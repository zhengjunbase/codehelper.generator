package com.ccnode.codegenerator.nextgenerationparser.parsedresult.delete;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedDeleteDto {
    private List<ParsedDelete> parsedDeletes;

    private List<ParsedDeleteError> errors;

    public List<ParsedDelete> getParsedDeletes() {
        return parsedDeletes;
    }

    public void setParsedDeletes(List<ParsedDelete> parsedDeletes) {
        this.parsedDeletes = parsedDeletes;
    }

    public List<ParsedDeleteError> getErrors() {
        return errors;
    }

    public void setErrors(List<ParsedDeleteError> errors) {
        this.errors = errors;
    }
}
