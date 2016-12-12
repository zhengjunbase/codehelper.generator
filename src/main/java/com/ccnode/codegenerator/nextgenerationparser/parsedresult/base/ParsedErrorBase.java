package com.ccnode.codegenerator.nextgenerationparser.parsedresult.base;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedErrorBase {
    protected Integer lastState;

    protected String remaining;



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
