package com.ccnode.codegenerator.jpaparse.parser;

import com.ccnode.codegenerator.jpaparse.*;
import com.ccnode.codegenerator.jpaparse.info.DeleteInfo;
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
public class DeleteParse extends BaseParser {


    public static String parse(String method, List<String> props, String tableName) {
        List<Term> terms = generateTerm(method, props);
        DeleteInfo info = buildUpdateInfo(terms);
        info.setTable(tableName);
        return buildDeleteSql(info);
    }

    private static String buildDeleteSql(DeleteInfo info) {
        return "\n\tdelete from " + "\n\t" + info.getTable() + "\n\t" + info.getQueryPart() + "\n";
    }

    private static DeleteInfo buildUpdateInfo(List<Term> terms) {
        DeleteInfo info = new DeleteInfo();
        int state = 0;
        int s = 0;
        while (s < terms.size()) {
            Term cur = terms.get(s);
            switch (state) {
                case 0: {
                    if (cur.getTermType() == TermType.START_OP) {
                        state = 2;
                        break;
                    } else {
                        throw new ParseException(cur, "method shall start with 'find', 'update' or 'delete'");
                    }
                }

                case 2: {
                    if (cur.getTermType().equals(TermType.BY)) {
                        info.setQueryPart(info.getQueryPart() + " where");
                        state = 3;
                        break;
                    } else {
                        throw new ParseException(cur, "shall use 'by' after delete");
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
                        throw new ParseException(cur, "shall use property of bean after by");
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
                        throw new ParseException(cur, "after property shall be compare operator, or 'and' or 'or'");
                    }
                }

                case 5: {
                    if (cur.getTermType() == TermType.LINK_OP) {
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue());
                        state = 3;
                        break;
                    } else {
                        throw new ParseException(cur, "after comparator shall be 'and' or 'or' or empty");
                    }
                }
            }
            s++;
        }
        if (state == 2 || state == 4 || state == 5) {
            return info;
        } else {
            throw new ParseException("the delete not end legal the query part is " + info.getQueryPart());
        }
    }

    private static List<Term> generateTerm(String method, List<String> props) {
        //first go to match with update.
        List<Term> terms = new ArrayList<Term>();
        int[] used = new int[method.length()];
        Map<Integer, Term> termMap = new HashMap<Integer, Term>();
        if (method.startsWith(KeyWordConstants.DELETE)) {
            for (int i = 0; i < KeyWordConstants.DELETE.length(); i++) {
                used[i] = 1;
                termMap.put(0, new Term(0, KeyWordConstants.DELETE.length(), TermType.START_OP, KeyWordConstants.DELETE));
            }
        } else {
            throw new ParseException("update not start with 'update'");
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
                    Term term = new Term((i - 1 - q.length()), i - 1, TermType.PROP, q);
                    throw new ParseException(term, " the property can't be found in bean or comparator etc, please check it");
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
        System.out.println(parse("DELETEBYUSERNAMEANDPASSWORDGRETERTHAN".toLowerCase(), props, "user"));
    }
}
