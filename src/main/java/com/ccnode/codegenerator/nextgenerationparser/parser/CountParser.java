package com.ccnode.codegenerator.nextgenerationparser.parser;

import com.ccnode.codegenerator.jpaparse.KeyWordConstants;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.count.ParsedCount;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.count.ParsedCountDto;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.count.ParsedCountError;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class CountParser extends BaseParser {
    private List<ParsedCount> counts;
    private List<ParsedCountError> errors;

    public CountParser(String methodName, List<String> props) {
        super(methodName, props);
    }

    public ParsedCountDto parse() {
        int state = 0;
        int len = KeyWordConstants.COUNT.length();
        ParsedCount count = new ParsedCount();
        parseMethods(state, methodName, len, count);
        ParsedCountDto dto = new ParsedCountDto();
        dto.setParsedCounts(this.counts);
        dto.setErrors(this.errors);
        return dto;
    }

    private void parseMethods(int state, String methodName, int len, ParsedCount count) {
        if (methodName.length() == len) {
            if (isValidEndState(state)) {
                counts.add(count);
            } else {
                ParsedCountError error = new ParsedCountError();
                error.setParsedCount(count);
                error.setLastState(state);
                error.setRemaining("");
                errors.add(error);
            }
        }

        String remaining = methodName.substring(len);
        boolean newParsedCount = false;

        switch (state){
            case 0:{
            }
        }
    }

    private boolean isValidEndState(int state) {
        return true;
    }
}
