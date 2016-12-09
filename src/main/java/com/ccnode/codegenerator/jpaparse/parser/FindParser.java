package com.ccnode.codegenerator.jpaparse.parser;


import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.jpaparse.*;
import com.ccnode.codegenerator.jpaparse.info.FindInfo;
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
public class FindParser extends BaseParser {
    // there are the order

    // make sure orderby matched before by. finddistinct before find.


    public static String parse(String methodName, List<String> props, String tableName) {
        //first try to split them into terms.
        List<Term> terms = generateTerm(methodName, props);
        FindInfo info = buildFindInfo(terms);
        info.setTable(tableName);
        return buildQueryBy(info);
    }

    private static String buildQueryBy(FindInfo info) {
        StringBuilder queryBuilder = new StringBuilder();
        if (!info.getDistinct()) {
            if (info.getAllField()) {
                queryBuilder.append("\n\tselect <include refid=\"" + MapperConstants.ALL_COLUMN + "\"/>");
            } else {
                queryBuilder.append("\n\tselect" + info.getFetchPart());
            }
        } else {
            queryBuilder.append("\n\tselect dictinct(" + info.getFetchPart() + ")");
        }
        queryBuilder.append("\n\tfrom " + info.getTable());
        queryBuilder.append("\n\t" + info.getQueryPart());
        return queryBuilder.toString();
    }

    private static FindInfo buildFindInfo(List<Term> terms) {
        FindInfo info = new FindInfo();
        int state = 0;
        int s = 0;
        while (s < terms.size()) {
            Term cur = terms.get(s);
            switch (state) {
                case 0: {
                    if (cur.getTermType() == TermType.START_OP) {
                        if (cur.getValue().equals(KeyWordConstants.FINDDISTINCT)) {
                            info.setDistinct(true);
                            state = 2;
                            break;
                        } else {
                            state = 1;
                            break;
                        }
                    } else {
                        throw new ParseException(cur, "index is not find or insert or delete");
                    }
                }
                case 1: {
                    if (cur.getTermType() == TermType.ORDERBY) {
                        info.setQueryPart(info.getQueryPart() + " order by");
                        state = 3;
                        break;
                    } else if (cur.getTermType() == TermType.PROP) {
                        info.setAllField(false);
                        info.setFetchPart(info.getFetchPart() + " " + cur.getValue());
                        state = 6;
                        break;
                    } else if (cur.getTermType() == TermType.BY) {
                        info.setQueryPart(" where");
                        state = 8;
                        break;
                    } else {
                        throw new ParseException(cur, "the term after find shall be property of bean or 'by' or 'orderby'");
                    }
                }
                case 2: {
                    if (cur.getTermType() == TermType.PROP) {
                        info.setFetchPart(cur.getValue());
                        state = 11;
                        break;
                    } else {
                        throw new ParseException(cur, "shall use property of bean after find distinct");
                    }
                }
                case 3: {
                    if (cur.getTermType() == TermType.PROP) {
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue());
                        state = 4;
                        break;
                    } else {
                        throw new ParseException(cur, "shall use property of bean after orderby");
                    }
                }
                case 4: {
                    if (cur.getTermType() == TermType.DESC) {
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue());
                        state = 5;
                        break;
                    } else {
                        //
                        throw new ParseException(cur, "shall be desc or asc after orderby with property, not support with multiple property order by now");
                    }
                }

                case 5: {
                    throw new ParseException(cur, "not support to multiple property order by now");
                }

                case 6: {
                    if (cur.getTermType() == TermType.BY) {
                        info.setQueryPart(" where");
                        state = 8;
                        break;
                    } else if (cur.getTermType() == TermType.ORDERBY) {
                        info.setQueryPart(info.getQueryPart() + " order by");
                        state = 3;
                        break;
                    } else if (cur.getTermType() == TermType.LINK_OP) {
                        info.setFetchPart(info.getFetchPart() + " " + cur.getValue());
                        state = 7;
                        break;
                    } else {
                        throw new ParseException(cur, "the term shall be orderby or 'and/or' after property of bean");
                    }
                }

                case 7: {
                    if (cur.getTermType() == TermType.PROP) {
                        info.setFetchPart(info.getFetchPart() + " " + cur.getValue());
                        state = 6;
                        break;
                    } else {
                        throw new ParseException(cur, "shall use with property of bean after 'and/or'");
                    }
                }
                case 8: {
                    if (cur.getTermType() == TermType.PROP) {
//                        shall add with equalPart.
                        Integer paramCount = info.getParamCount();
                        String equalPart = " =#{" + paramCount + "}";
                        info.setParamCount(info.getParamCount() + 1);
                        info.setLastEqualLength(equalPart.length());
                        info.setLastQueryProp(cur.getValue());
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue() + equalPart);
                        state = 9;
                        break;
                    } else {
                        throw new ParseException(cur,"shall use property of bean after 'by'" );
                    }
                }

                case 9: {
                    if (cur.getTermType() == TermType.ORDERBY) {
                        info.setQueryPart(info.getQueryPart() + " order by");
                        state = 3;
                        break;
                    } else if (cur.getTermType() == TermType.COMPARE_OP) {
                        //first need to roll back the equal operator.
                        info.setParamCount(info.getParamCount() - 1);
                        info.setQueryPart(info.getQueryPart().substring(0, info.getQueryPart().length() - info.getLastEqualLength()));
                        handleWithCompare(info, cur);
                        state = 10;
                        break;
                    } else if (cur.getTermType() == TermType.LINK_OP) {
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue());
                        state = 8;
                        break;
                    } else {
                        throw new ParseException(cur,"shall use with 'orderby' or 'compartor' or 'and/or' after property");
                    }
                }
                case 10: {
                    if (cur.getTermType() == TermType.ORDERBY) {
                        info.setQueryPart(info.getQueryPart() + " order by");
                        state = 3;
                        break;
                    } else if (cur.getTermType() == TermType.LINK_OP) {
                        info.setQueryPart(info.getQueryPart() + " " + cur.getValue());
                        state = 8;
                        break;
                    } else {
                        throw new ParseException(cur,"shall use 'orderby' or 'and/or' after comparator");
                    }
                }
                case 11: {
                    if (cur.getTermType() == TermType.ORDERBY) {
                        info.setQueryPart(info.getQueryPart() + " order by");
                        state = 3;
                        break;
                    } else if (cur.getTermType() == TermType.BY) {
                        info.setQueryPart(info.getQueryPart() + " where");
                        state = 8;
                        break;
                    } else {
                        throw new ParseException(cur,"shall use with 'orderby' or 'by' or empty after select distinct(property) ");
                    }
                }
            }
            s++;
        }
        if (state == 1 || state == 4 || state == 5 || state == 9 || state == 10 || state == 11 || state == 6) {
            return info;
        } else {
            throw new ParseException("the query not end legal, the fetch part is " + info.getFetchPart() + " the query part is " + info.getQueryPart());
        }
    }


    /**
     * @param methodName
     * @return generate term to use with. there is time to check with them.
     * the compareOp shall after by keyword.  and desc after order by keyword.
     */
    private static List<Term> generateTerm(String methodName, List<String> props) {
        Map<Integer, Term> termMaps = new HashMap<Integer, Term>();
        List<Term> terms = new ArrayList<Term>();
        //try to match with things.
        boolean isBy = false;
        boolean isOrderBy = false;

        int used[] = new int[methodName.length()];
        //first parse with finds.
        for (String find : finds) {
            //find first to match with it.
            if (methodName.startsWith(find)) {
                for (int i = 0; i < find.length(); i++) {
                    used[i] = 1;
                }
                Term e = new Term(0, find.length(), TermType.START_OP, find);
                termMaps.put(0, e);
                break;
            }
        }
        //those that only exist one time.
        //first check with orderBy.
        // than check with props
        for (String prop : props) {
            Pattern propPattern = PatternUtils.getPattern(prop.toLowerCase());
            Matcher propMatcher = propPattern.matcher(methodName);
            while (propMatcher.find()) {
                int start = propMatcher.start();
                int end = propMatcher.end();
                if (used[start] != 1 && used[end - 1] != 1) {
                    //then add compare to term.
                    for (int i = start; i < end; i++) {
                        used[i] = 1;
                    }
                    Term e = new Term(start, end, TermType.PROP, GenCodeUtil.getUnderScore(prop));
                    termMaps.put(start, e);
                }
            }
        }
        int orderByStart = methodName.indexOf(KeyWordConstants.ORDERBY);
        if (orderByStart != -1) {
            isOrderBy = true;
            for (int i = orderByStart; i < orderByStart + KeyWordConstants.ORDERBY.length(); i++) {
                used[i] = 1;
            }
            Term e = new Term(orderByStart, orderByStart + KeyWordConstants.ORDERBY.length(), TermType.ORDERBY, KeyWordConstants.ORDERBY);
            termMaps.put(orderByStart, e);
        }

        //than check with by.  only find one time.
        Pattern by = PatternUtils.getPattern(KeyWordConstants.BY);
        Matcher matcher = by.matcher(methodName);
        while (matcher.find()) {
            int start = matcher.start();
            if (used[start] != 1) {
                int end = matcher.end();
                for (int i = start; i < end; i++) {
                    used[i] = 1;
                }
                isBy = true;
                Term e = new Term(start, end, TermType.BY, KeyWordConstants.BY);
                termMaps.put(start, e);
                break;
            }
        }

        //than find with and and or.
        for (String link : linkOp) {
            Pattern linkPattern = PatternUtils.getPattern(link);
            Matcher andMatcher = linkPattern.matcher(methodName);
            while (andMatcher.find()) {
                int start = andMatcher.start();
                if (used[start] != 1) {
                    int end = andMatcher.end();
                    for (int i = start; i < end; i++) {
                        used[i] = 1;
                    }
                    Term e = new Term(start, end, TermType.LINK_OP, link);
                    termMaps.put(start, e);
                }
            }
        }

        //if is by then shall check with comparator.
        if (isBy) {
            for (String compare : compareOp) {
                Pattern comparePattern = PatternUtils.getPattern(compare);
                Matcher compareMatcher = comparePattern.matcher(methodName);
                while (compareMatcher.find()) {
                    int start = compareMatcher.start();
                    int end = compareMatcher.end();
                    if (used[start] != 1 && used[end - 1] != 1) {
                        //then add compare to term.
                        for (int i = start; i < end; i++) {
                            used[i] = 1;
                        }
                        Term e = new Term(start, end, TermType.COMPARE_OP, compare);
                        termMaps.put(start, e);
                    }
                }
            }
        }
        //shall check the order time.
        if (isOrderBy) {
            for (String ordertype : order) {
                Pattern orderTypePattern = PatternUtils.getPattern(ordertype);
                Matcher orderTypeMatcher = orderTypePattern.matcher(methodName);
                while (orderTypeMatcher.find()) {
                    int start = orderTypeMatcher.start();
                    int end = orderTypeMatcher.end();
                    if (used[start] != 1 && used[end - 1] != 1) {
                        for (int i = start; i < end; i++) {
                            used[i] = 1;
                        }
                        Term term = new Term(start, end, TermType.DESC, ordertype);
                        termMaps.put(start, term);
                    }
                }
            }
        }

        int i = 0;
        String q = "";
        while (i < methodName.length()) {
            if (used[i] == 1) {
                if (q.length() > 0) {
                    Term term  = new Term((i-1-q.length()),i-1,TermType.PROP,q);
                    throw new ParseException(term,"can't parse the part");
//                    terms.add(new Term(0, 0, TermType.PROP, q));
//                    q = "";
                }
                terms.add(termMaps.get(i));
                i = termMaps.get(i).getEnd();
            } else {
                q += methodName.charAt(i);
                i++;
            }
        }
        // than go to create the basic term. then add them to the queud.
        return terms;
    }


    public static void main(String[] args) {
        List<String> ll = new ArrayList<String>();
        ll.add("UserId");
        ll.add("password");
        ll.add("username");
        List<Term> terms = generateTerm("FINDDISTINCTUSERIDANDPASSWORDBYUSERNAMELIKEORDERBYUSERIDDESC".toLowerCase(), ll);
        System.out.println(parse("FINDUSERIDANDUSERNAMEBYUSERNAMELIKEANDUSERNAMEGREATERTHANANDPASSWORDBETWEENORDERBYUSERID".toLowerCase(), ll, "user"));

    }
}
