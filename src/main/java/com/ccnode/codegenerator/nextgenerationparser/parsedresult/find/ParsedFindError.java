package com.ccnode.codegenerator.nextgenerationparser.parsedresult.find;

import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.ParsedErrorBase;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedFindError extends ParsedErrorBase{
    private ParsedFind parsedFind;

    public ParsedFind getParsedFind() {
        return parsedFind;
    }

    public void setParsedFind(ParsedFind parsedFind) {
        this.parsedFind = parsedFind;
    }


}
