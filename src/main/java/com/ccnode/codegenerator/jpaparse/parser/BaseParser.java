package com.ccnode.codegenerator.jpaparse.parser;


import com.ccnode.codegenerator.jpaparse.KeyWordConstants;
import com.ccnode.codegenerator.jpaparse.ParseException;
import com.ccnode.codegenerator.jpaparse.Term;
import com.ccnode.codegenerator.jpaparse.TermType;
import com.ccnode.codegenerator.jpaparse.info.BaseInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/5.
 */
public  class BaseParser {
    protected static String[] finds = {KeyWordConstants.FINDDISTINCT, KeyWordConstants.FIND};

    protected static String[] linkOp = {KeyWordConstants.AND, KeyWordConstants.OR};

    protected static String[] compareOp = {KeyWordConstants.BETWEEN, KeyWordConstants.GREATERTHAN, KeyWordConstants.LESSTHAN,
            KeyWordConstants.ISNOTNULL, KeyWordConstants.ISNULL, KeyWordConstants.NOTNULL, KeyWordConstants.NOTLIKE, KeyWordConstants.LIKE
            , KeyWordConstants.NOTIN, KeyWordConstants.NOT, KeyWordConstants.IN};

    protected static String[] order = {KeyWordConstants.ASC, KeyWordConstants.DESC};

    protected static void handleWithCompare(BaseInfo info, Term cur) {
        switch (cur.getValue()) {
            case KeyWordConstants.GREATERTHAN: {
                info.setQueryPart(info.getQueryPart() + " "+cdata(">")+" #{" + info.getParamCount() + "}");
                info.setParamCount(info.getParamCount() + 1);
                break;
            }
            case KeyWordConstants.LESSTHAN: {
                info.setQueryPart(info.getQueryPart() + " "+cdata("<")+" #{" + info.getParamCount() + "}");
                info.setParamCount(info.getParamCount() + 1);
                break;
            }
            case KeyWordConstants.BETWEEN: {
                info.setQueryPart(info.getQueryPart() + " "+cdata(">=")+" #{" + info.getParamCount() + "} and " + info.getLastQueryProp() + " "+cdata("<=")+" #{" + (info.getParamCount() + 1) + "}");
                info.setParamCount(info.getParamCount() + 2);
                break;
            }
            case KeyWordConstants.ISNOTNULL: {
                info.setQueryPart(info.getQueryPart() + " is not null");
                break;
            }
            case KeyWordConstants.ISNULL: {
                info.setQueryPart(info.getQueryPart() + " is null");
                info.setParamCount(info.getParamCount());
                break;
            }
            case KeyWordConstants.NOT: {
                info.setQueryPart(info.getQueryPart() + " != #{" + info.getParamCount() + "}");
                info.setParamCount(info.getParamCount() + 1);
                break;
            }
            case KeyWordConstants.NOTIN: {
                info.setQueryPart(info.getQueryPart() + " not in #{" + info.getParamCount() + "}");
                info.setParamCount(info.getParamCount() + 1);
                break;
            }
            case KeyWordConstants.IN: {
                info.setQueryPart(info.getQueryPart() + " in #{" + info.getParamCount() + "}");
                info.setParamCount(info.getParamCount() + 1);
                break;
            }
            case KeyWordConstants.NOTLIKE: {
                info.setQueryPart(info.getQueryPart() + " not like #{" + info.getParamCount() + "}");
                info.setParamCount(info.getParamCount() + 1);
                break;
            }
            case KeyWordConstants.LIKE: {
                info.setQueryPart(info.getQueryPart() + " like #{" + info.getParamCount() + "}");
                info.setParamCount(info.getParamCount() + 1);
                break;
            }

        }
    }

    public static String cdata(String s){
        return "<![CDATA["+s+"]]>";
    }


    protected static List<Term> buildTerms(String methodName, Map<Integer, Term> termMaps, int[] used) {
        List<Term> terms = new ArrayList<Term>();
        int i = 0;
        String q = "";
        while (i < methodName.length()) {
            if (used[i] == 1) {
                if (q.length() > 0) {
                    Term term = new Term((i-q.length()), i, TermType.PROP, q);
                    throw new ParseException(term, "can't parse the part");

                }
                terms.add(termMaps.get(i));
                i = termMaps.get(i).getEnd();
            } else {
                q += methodName.charAt(i);
                i++;
            }
        }
        if (q.length() > 0) {
            Term term = new Term((i -q.length()), i, TermType.PROP, q);
            throw new ParseException(term, "can't parse the part");
        }

        return terms;
    }
}
