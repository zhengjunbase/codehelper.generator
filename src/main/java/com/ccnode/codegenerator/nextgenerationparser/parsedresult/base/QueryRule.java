package com.ccnode.codegenerator.nextgenerationparser.parsedresult.base;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryRule {

    /**
     * the property to query
     */
    private String prop;


    /**
     * the operator like 'greaterthan/lessthan/between ect.
     */
    private String operator;


    /**
     * the connector for 'and/or'
     */
    private String connector;


    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }
}
