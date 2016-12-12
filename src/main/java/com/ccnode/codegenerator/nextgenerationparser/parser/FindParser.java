package com.ccnode.codegenerator.nextgenerationparser.parser;

import com.ccnode.codegenerator.jpaparse.KeyWordConstants;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.ParsedFindError;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.ParsedFind;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.ParsedFindDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class FindParser extends BaseParser {

    private String methodName;

    private String[] props;

    private String[] lowerProps;

    List<ParsedFind> finds = new ArrayList<>();
    List<ParsedFindError> errors = new ArrayList<>();

    public FindParser(String methodName, List<String> props) {
        this.methodName = methodName;
        this.props = new String[props.size()];
        this.lowerProps = new String[props.size()];
        for (int i = 0; i < props.size(); i++) {
            this.props[i] = props.get(i);
            this.lowerProps[i] = props.get(i).toLowerCase();
        }
    }

    public ParsedFindDto parse() {
        int state = 0;
        int len = KeyWordConstants.FIND.length();
        ParsedFind parsedFind = new ParsedFind();
        parseMethods(state, methodName, len, parsedFind);
        ParsedFindDto dto = new ParsedFindDto();
        dto.setParsedFinds(finds);
        dto.setParsedFindErrors(errors);
        return dto;
    }

    private void parseMethods(int state, String methodName, int len, ParsedFind parsedFind) {
        if (methodName.length() == len) {
            //means there is no other letter to parse. // need to exit.
            if (isValidEndState(state)) {
                finds.add(parsedFind);
            } else {
                ParsedFindError error = new ParsedFindError();
                error.setParsedFind(parsedFind);
                error.setLastState(state);
                error.setRemaining("");
                errors.add(error);
            }
            return;
        }
        String remaining = methodName.substring(len);
        boolean newParseFind = false;
        //check with state.
        switch (state) {
            case 0: {
                if (remaining.startsWith(KeyWordConstants.DISTINCT)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    newFind.setDistinct(true);
                    parseMethods(1, remaining, KeyWordConstants.DISTINCT.length(), newFind);
                    newParseFind = true;
                }
                if (remaining.startsWith(KeyWordConstants.FIRST)) {
                    if (remaining.length() == KeyWordConstants.FIRST.length()) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.setLimit(1);
                        parseMethods(1, remaining, KeyWordConstants.FIRST.length(), newFind);
                        newParseFind = true;
                    } else {
                        int limitCount = 0;
                        int i;
                        for (i = KeyWordConstants.FIRST.length(); i < remaining.length(); i++) {
                            char c = remaining.charAt(i);
                            if (c >= '0' && c <= '9') {
                                limitCount = limitCount * 10 + (c - '0');
                            } else {
                                break;
                            }
                        }
                        if (limitCount == 0) {
                            limitCount = 1;
                        }
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.setLimit(limitCount);
                        parseMethods(2, remaining, i, newFind);
                        newParseFind = true;
                    }
                }
//                check for props.
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFetchProps(props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }
                break;
            }
            case 1: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFetchProps(props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }
            case 2: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFetchProps(props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }
                break;
            }
            case 3: {
                if (remaining.startsWith(KeyWordConstants.AND)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(7, remaining, KeyWordConstants.AND.length(), newFind);
                    newParseFind = true;
                }
                if (remaining.startsWith(KeyWordConstants.BY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(8, remaining, KeyWordConstants.BY.length(), newFind);
                    newParseFind = true;
                }
                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }
                break;
            }
            case 4: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addOrderByProp(props[i]);
                        parseMethods(5, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }
            case 5: {
                for (String orderbyw : order) {
                    if (remaining.startsWith(orderbyw)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addOrderByPropOrder(orderbyw);
                        parseMethods(6, remaining, orderbyw.length(), newFind);
                        newParseFind = true;
                    }
                }

                if (remaining.startsWith(KeyWordConstants.AND)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(4, remaining, KeyWordConstants.AND.length(), newFind);
                    newParseFind = true;
                }
                break;
            }
            case 6: {
                if (remaining.startsWith(KeyWordConstants.AND)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(4, remaining, KeyWordConstants.AND.length(), newFind);
                    newParseFind = true;
                }
                break;
            }

            case 7: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addFetchProps(props[i]);
                        parseMethods(3, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }

            case 8: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addQueryProp(props[i]);
                        parseMethods(9, remaining, props[i].length(), newFind);
                        newParseFind = true;
                    }
                }
                break;
            }

            case 9: {
                for (String comp : compareOp) {
                    if (remaining.startsWith(comp)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addQueryOperator(comp);
                        parseMethods(10, remaining, comp.length(), newFind);
                        newParseFind = true;
                    }
                }

                for (String link : linkOp) {
                    if (remaining.startsWith(link)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addConnector(link);
                        parseMethods(8, remaining, link.length(), newFind);
                    }
                }

                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }
                break;

            }

            case 10: {
                if (remaining.startsWith(KeyWordConstants.ORDERBY)) {
                    ParsedFind newFind = createParseFind(parsedFind);
                    parseMethods(4, remaining, KeyWordConstants.ORDERBY.length(), newFind);
                    newParseFind = true;
                }
                for (String link : linkOp) {
                    if (remaining.startsWith(link)) {
                        ParsedFind newFind = createParseFind(parsedFind);
                        newFind.addConnector(link);
                        parseMethods(8, remaining, link.length(), newFind);
                    }
                }
                break;
            }
        }

        if (!newParseFind) {
            //means there can find no match for the current info.
            ParsedFindError error = new ParsedFindError();
            error.setParsedFind(parsedFind);
            error.setRemaining(remaining);
            error.setLastState(state);
            //no need for the depth. get the remaining is ok.
            errors.add(error);
        }

    }

    private ParsedFind createParseFind(ParsedFind parsedFind) {
        return parsedFind.clone();
    }

    private boolean isValidEndState(int state) {
        if (state == 2 || state == 3 || state == 5 || state == 6 || state == 9 || state == 10) {
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        List<String> props = new ArrayList<>();
        props.add("hello");
        props.add("first");
        props.add("id");
        props.add("name");
        props.add("order");
        String methodName = "findFirst10OrderByName";
        FindParser findParser = new FindParser(methodName.toLowerCase(), props);
        ParsedFindDto parse = findParser.parse();
        parse.getParsedFinds();
    }
}