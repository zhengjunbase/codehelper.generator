package com.ccnode.codegenerator.nextgenerationparser.parsedresult.count;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedCountDto {
    private List<ParsedCount> parsedCounts;

    private List<ParsedCountError> errors;

    public List<ParsedCount> getParsedCounts() {
        return parsedCounts;
    }

    public void setParsedCounts(List<ParsedCount> parsedCounts) {
        this.parsedCounts = parsedCounts;
    }

    public List<ParsedCountError> getErrors() {
        return errors;
    }

    public void setErrors(List<ParsedCountError> errors) {
        this.errors = errors;
    }
}
