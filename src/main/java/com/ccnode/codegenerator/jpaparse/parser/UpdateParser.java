package com.ccnode.codegenerator.jpaparse.parser;

import com.ccnode.codegenerator.jpaparse.*;
import com.ccnode.codegenerator.jpaparse.info.UpdateInfo;
import com.ccnode.codegenerator.util.GenCodeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bruce.ge on 2016/12/4.
 */
public class UpdateParser extends AbstractParser {


    public static String parse(String method, List<String> props, String tableName) {
        List<Term> terms = generateTerm(method, props);
        UpdateInfo info = buildUpdateInfo(terms);
        info.setTable(tableName);
        return buildUpdateByInfo(info);
    }

    private static String buildUpdateByInfo(UpdateInfo info) {
        return "update " + info.getTable() + " set " + info.getUpdatePart() + info.getQueryPart();
    }

    private static UpdateInfo buildUpdateInfo(List<Term> terms) {
        UpdateInfo info = new UpdateInfo();
        int state = 0;
        int s = 0;
        while (s < terms.size()) {
            Term cur = terms.get(s);
            switch (state) {
                case 0: {
                    if (cur.getTermType() == TermType.START_OP) {
                        state = 1;
                        break;
                    } else {
                        throw new ParseException("shall start with find, update or delete, the current value is " + cur.getValue());
                    }
                }
                case 1: {
                    if (cur.getTermType() == TermType.PROP) {
                        info.setUpdatePart(info.getUpdatePart() + " " + cur.getValue() + "=#{" + info.getParamCount() + "}");
                        info.setParamCount(info.getParamCount() + 1);
                        state = 2;
                        break;
                    } else {
                        throw new ParseException("current element is not filed of bean the value is " + cur.getValue());
                    }
                }
                case 2: {
                    if (cur.getValue().equals(KeyWordConstants.AND)) {
                        info.setUpdatePart(info.getUpdatePart() + ", ");
                        state = 1;
                        break;
                    } else if (cur.getTermType().equals(TermType.BY)) {
                        info.setQueryPart(info.getQueryPart() + " where");
                        state = 3;
                        break;
                    } else {
                        throw new ParseException("after field in bean shall be by or and ,the cur value is " + cur.getValue());
                    }
                }
                case 3: {
                    if (cur.getTermType() == TermType.PROP) {
                        Integer paramCount = info.getParamCount();
                        String equalPart = " =" + "#{" + paramCount + "}";
                        info.setParamCount(info.getParamCount() + 1);
                        info.setLastEqualLength(equalPart.length());
                        info.setLastQueryProp(cur.getValue());
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue() + equalPart);
                        state = 4;
                        break;
                    } else {
                        throw new ParseException("use filed after by, the current vlaue is " + cur.getValue());
                    }
                }
                case 4: {
                    if (cur.getTermType() == TermType.COMPARE_OP) {
                        info.setParamCount(info.getParamCount() - 1);
                        info.setQueryPart(info.getQueryPart().substring(0, info.getQueryPart().length() - info.getLastEqualLength()));
                        handleWithCompare(info, cur);
                        state = 5;
                        break;
                    } else if (cur.getTermType() == TermType.LINK_OP) {
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue());
                        state = 3;
                        break;
                    } else {
                        throw new ParseException("query field after prop not legal, the term is " + cur.getValue());
                    }
                }

                case 5: {
                    if (cur.getTermType() == TermType.LINK_OP) {
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue());
                        state = 3;
                        break;
                    } else {
                        throw new ParseException("the term after compare is not legal, the term is " + cur.getValue());
                    }
                }
            }
            s++;
        }
        if (state == 2 || state == 4 || state == 5) {
            return info;
        } else {
            throw new ParseException("the update not end legal, the update part is " + info.getUpdatePart() + " the query part is " + info.getQueryPart());
        }
    }

    private static List<Term> generateTerm(String method, List<String> props) {
        //first go to match with update.
        List<Term> terms = new ArrayList<Term>();
        int[] used = new int[method.length()];
        Map<Integer, Term> termMap = new HashMap<Integer, Term>();
        if (method.startsWith(KeyWordConstants.UPDATE)) {
            for (int i = 0; i < KeyWordConstants.UPDATE.length(); i++) {
                used[i] = 1;
                termMap.put(0, new Term(0, KeyWordConstants.UPDATE.length(), TermType.START_OP, KeyWordConstants.UPDATE));
            }
        } else {
            throw new ParseException("update not start with update not legal");
        }
        for (String prop : props) {
            Pattern pattern = PatternUtils.getPattern(prop.toLowerCase());
            Matcher matcher = pattern.matcher(method);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                if (used[start] != 1 && used[end - 1] != 1) {
                    for (int i = start; i < end; i++) {
                        used[i] = 1;
                    }
                    termMap.put(start, new Term(start, end, TermType.PROP, GenCodeUtil.getUnderScore(prop)));
                }
            }
        }
        boolean isBy = false;
        Pattern by = PatternUtils.getPattern(KeyWordConstants.BY);
        Matcher matcher = by.matcher(method);
        while (matcher.find()) {
            int start = matcher.start();
            if (used[start] != 1) {
                int end = matcher.end();
                for (int i = start; i < end; i++) {
                    used[i] = 1;
                }
                isBy = true;
                Term e = new Term(start, end, TermType.BY, KeyWordConstants.BY);
                termMap.put(start, e);
                break;
            }
        }

        //than find with and and or.
        for (String link : linkOp) {
            Pattern linkPattern = PatternUtils.getPattern(link);
            Matcher andMatcher = linkPattern.matcher(method);
            while (andMatcher.find()) {
                int start = andMatcher.start();
                if (used[start] != 1) {
                    int end = andMatcher.end();
                    for (int i = start; i < end; i++) {
                        used[i] = 1;
                    }
                    Term e = new Term(start, end, TermType.LINK_OP, link);
                    termMap.put(start, e);
                }
            }
        }

        if (isBy) {
            for (String compare : compareOp) {
                Pattern comparePattern = PatternUtils.getPattern(compare);
                Matcher compareMatcher = comparePattern.matcher(method);
                while (compareMatcher.find()) {
                    int start = compareMatcher.start();
                    int end = compareMatcher.end();
                    if (used[start] != 1 && used[end - 1] != 1) {
                        //then add compare to term.
                        for (int i = start; i < end; i++) {
                            used[i] = 1;
                        }
                        Term e = new Term(start, end, TermType.COMPARE_OP, compare);
                        termMap.put(start, e);
                    }
                }
            }
        }

        int i = 0;
        String q = "";
        while (i < method.length()) {
            if (used[i] == 1) {
                if (q.length() > 0) {
                    throw new ParseException(" the property: '" + q + "' not in bean, the index range of wrong property is start with " + (i - 1 - q.length()) + " and end with " + (i - 1));
//                    terms.add(new Term(0, 0, TermType.PROP, q));
//                    q = "";
                }
                terms.add(termMap.get(i));
                i = termMap.get(i).getEnd();
            } else {
                q += method.charAt(i);
                i++;
            }
        }
        // than go to create the basic term. then add them to the queud.
        return terms;
    }


    public static void main(String[] args) {
        List<String> props = new ArrayList<>();
        props.add("username");
        props.add("password");
        System.out.println(parse("UPDATEUSERNAMEANDPASSWORDBYUSERNAMEGREATERTHAN".toLowerCase(), props, "user"));
    }
}
