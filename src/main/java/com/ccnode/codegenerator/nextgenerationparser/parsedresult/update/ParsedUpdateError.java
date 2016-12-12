package com.ccnode.codegenerator.nextgenerationparser.parsedresult.update;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedUpdateError {

    private Integer lastState;

    private String remaining;

    private ParsedUpdate parsedUpdate;


    public ParsedUpdate getParsedUpdate() {
        return parsedUpdate;
    }

    public void setParsedUpdate(ParsedUpdate parsedUpdate) {
        this.parsedUpdate = parsedUpdate;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }


    public Integer getLastState() {
        return lastState;
    }

    public void setLastState(Integer lastState) {
        this.lastState = lastState;
    }
}
