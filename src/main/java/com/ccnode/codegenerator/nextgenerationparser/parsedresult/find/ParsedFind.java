package com.ccnode.codegenerator.nextgenerationparser.parsedresult.find;

import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.ParsedBase;
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedFind extends ParsedBase{
    /**
     * the part after 'select' before 'by'
     */
    private List<String> fetchProps;

    /**
     * the part after by.
     */

    /**
     * the select distinct.
     */
    private Boolean distinct = false;

    /**
     *
     */
    private Integer limit = -1;


    List<OrderByRule> orderByProps;

    public ParsedFind clone() {
        return Cloner.standard().deepClone(this);
    }

    public void addFetchProps(String props) {
        if (fetchProps == null) {
            fetchProps = new ArrayList<>();
        }
        fetchProps.add(props);
    }


    public void addOrderByProp(String prop) {
        if (orderByProps == null) {
            orderByProps = new ArrayList<>();
        }
        OrderByRule rule = new OrderByRule();
        rule.setProp(prop);
        orderByProps.add(rule);
    }

    public void addOrderByPropOrder(String order) {
        OrderByRule rule = this.orderByProps.get(this.orderByProps.size() - 1);
        rule.setOrder(order);
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
