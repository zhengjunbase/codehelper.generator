package com.ccnode.codegenerator.nextgenerationparser.parsedresult.find;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedFindDto {
    private List<ParsedFind> parsedFinds;

    private List<ParsedFindError> parsedFindErrors;

    public List<ParsedFind> getParsedFinds() {
        return parsedFinds;
    }

    public void setParsedFinds(List<ParsedFind> parsedFinds) {
        this.parsedFinds = parsedFinds;
    }

    public List<ParsedFindError> getParsedFindErrors() {
        return parsedFindErrors;
    }

    public void setParsedFindErrors(List<ParsedFindError> parsedFindErrors) {
        this.parsedFindErrors = parsedFindErrors;
    }
}
