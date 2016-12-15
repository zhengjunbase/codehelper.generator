package com.ccnode.codegenerator.nextgenerationparser.parser;

import com.ccnode.codegenerator.jpaparse.KeyWordConstants;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.delete.ParsedDelete;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.delete.ParsedDeleteDto;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.delete.ParsedDeleteError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class DeleteParser extends BaseParser {

    private List<ParsedDelete> parsedDeletes = new ArrayList<>();

    private List<ParsedDeleteError> errors = new ArrayList<>();

    public DeleteParser(String methodName, List<String> props) {
        super(methodName, props);
    }

    public ParsedDeleteDto parse() {
        int state = 0;
        int len = KeyWordConstants.DELETE.length();
        ParsedDelete parsedDelete = new ParsedDelete();
        parseMethods(state, methodName, len, parsedDelete);
        ParsedDeleteDto dto = new ParsedDeleteDto();
        dto.setParsedDeletes(parsedDeletes);
        dto.setErrors(errors);
        return dto;
    }

    private void parseMethods(int state, String methodName, int len, ParsedDelete parsedDelete) {
        if (methodName.length() == len) {
            if (isValidState(state)) {
                parsedDeletes.add(parsedDelete);
            } else {
                ParsedDeleteError error = new ParsedDeleteError();
                error.setParsedDelete(parsedDelete);
                error.setRemaining("");
                error.setLastState(state);
                errors.add(error);
            }
            return;
        }
        String remaining = methodName.substring(len);

        boolean newParseDelete = false;

        switch (state) {
            case 0: {
                if (remaining.startsWith(KeyWordConstants.BY)) {
                    ParsedDelete newDelete = parsedDelete.clone();
                    parseMethods(1, remaining, KeyWordConstants.BY.length(), newDelete);
                    newParseDelete = true;
                }
                break;
            }
            case 1: {
                for (int i = 0; i < props.length; i++) {
                    if (remaining.startsWith(lowerProps[i])) {
                        ParsedDelete clone = parsedDelete.clone();
                        clone.addQueryProp(props[i]);
                        parseMethods(2, remaining, props[i].length(), clone);
                        newParseDelete = true;
                    }
                }
                break;
            }
            case 2: {
                for (String link : linkOp) {
                    if (remaining.startsWith(link)) {
                        ParsedDelete clone = parsedDelete.clone();
                        clone.addConnector(link);
                        parseMethods(1, remaining, link.length(), clone);
                        newParseDelete = true;
                    }
                }

                for (String comp : compareOp) {
                    if (remaining.startsWith(comp)) {
                        ParsedDelete clone = parsedDelete.clone();
                        clone.addQueryOperator(comp);
                        parseMethods(3, remaining, comp.length(), clone);
                        newParseDelete = true;
                    }
                }
                break;
            }

            case 3: {
                for (String link : linkOp) {
                    if (remaining.startsWith(link)) {
                        ParsedDelete clone = parsedDelete.clone();
                        clone.addConnector(link);
                        parseMethods(1, remaining, link.length(), clone);
                        newParseDelete = true;
                    }
                }
                break;
            }

        }
        if (!newParseDelete) {
            ParsedDeleteError error = new ParsedDeleteError();
            error.setParsedDelete(parsedDelete);
            error.setRemaining(remaining);
            error.setLastState(state);
            errors.add(error);
        }

    }

    private boolean isValidState(int state) {
        if (state == 0 || state == 2 || state == 3) {
            return true;
        }
        return false;
    }
}
