package com.ccnode.codegenerator.nextgenerationparser.parsedresult.delete;

import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.ParsedErrorBase;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedDeleteError extends ParsedErrorBase{
    private ParsedDelete parsedDelete;

    public ParsedDelete getParsedDelete() {
        return parsedDelete;
    }

    public void setParsedDelete(ParsedDelete parsedDelete) {
        this.parsedDelete = parsedDelete;
    }
}
