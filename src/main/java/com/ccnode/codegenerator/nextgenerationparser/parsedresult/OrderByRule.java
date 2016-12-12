package com.ccnode.codegenerator.nextgenerationparser.parsedresult;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class OrderByRule {
    private String prop;


    /**
     * desc or asc default is asc.
     */
    private String order = "asc";


    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
