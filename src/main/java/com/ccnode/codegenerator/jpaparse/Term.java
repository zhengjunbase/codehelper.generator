package com.ccnode.codegenerator.jpaparse;

/**
 * Created by bruce.ge on 2016/12/4.
 */
public class Term {
    private int start;

    private int end;

    private TermType termType;

    private String value;

    public Term(int start, int end, TermType termType, String value) {
        this.start = start;
        this.end = end;
        this.termType = termType;
        this.value = value;
    }


    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public TermType getTermType() {
        return termType;
    }

    public void setTermType(TermType termType) {
        this.termType = termType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
