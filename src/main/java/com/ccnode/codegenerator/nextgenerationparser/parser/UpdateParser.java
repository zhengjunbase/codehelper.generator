package com.ccnode.codegenerator.nextgenerationparser.parser;

import com.ccnode.codegenerator.jpaparse.KeyWordConstants;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.update.ParsedUpdateDto;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.update.ParsedUpdateError;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.update.ParsedUpdate;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class UpdateParser extends BaseParser {

    private List<ParsedUpdate> parsedUpdates;

    private List<ParsedUpdateError> errors;

    public UpdateParser(String methodName, List<String> props) {
        super(methodName, props);
    }

    ParsedUpdateDto parse() {
        int state = 0;
        int len = KeyWordConstants.UPDATE.length();
        ParsedUpdate parsedUpdate = new ParsedUpdate();
        parseMethods(state, methodName, len, parsedUpdate);
        ParsedUpdateDto dto = new ParsedUpdateDto();
        dto.setUpdateList(parsedUpdates);
        dto.setErrorList(errors);
        return dto;
    }

    private void parseMethods(int state, String methodName, int len, ParsedUpdate parsedUpdate) {
        if (methodName.length() == len) {
            if (isValidState(state)) {
                parsedUpdates.add(parsedUpdate);
            } else {
                ParsedUpdateError error = new ParsedUpdateError();
                error.setParsedUpdate(parsedUpdate);
                error.setLastState(state);
                error.setRemaining("");
                errors.add(error);
            }
            return;
        }
        String remaining = methodName.substring(len);
        boolean newParseFind = false;
        //check with state.

    }

    private boolean isValidState(int state) {
        return true;
    }


}
