package com.ccnode.codegenerator.jpaparse;

/**
 * Created by bruce.ge on 2016/12/4.
 */
public class ParseException extends RuntimeException {

    private Term term;

    public ParseException() {
        super();
    }

    public ParseException(String message) {
        super(message);
    }


    public ParseException(Term term, String message) {
        super(message);
        this.term = term;
    }


    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
