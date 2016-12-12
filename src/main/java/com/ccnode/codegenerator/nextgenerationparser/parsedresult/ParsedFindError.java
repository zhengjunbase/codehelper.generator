package com.ccnode.codegenerator.nextgenerationparser.parsedresult;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedFindError {
    private ParsedFind parsedFind;

    private Integer lastState;

    private String remaining;



    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }



    public ParsedFind getParsedFind() {
        return parsedFind;
    }

    public void setParsedFind(ParsedFind parsedFind) {
        this.parsedFind = parsedFind;
    }

    public Integer getLastState() {
        return lastState;
    }

    public void setLastState(Integer lastState) {
        this.lastState = lastState;
    }

}
