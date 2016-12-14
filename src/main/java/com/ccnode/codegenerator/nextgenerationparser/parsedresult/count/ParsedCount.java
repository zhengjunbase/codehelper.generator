package com.ccnode.codegenerator.nextgenerationparser.parsedresult.count;

import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.ParsedBase;
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedCount extends ParsedBase{
    private boolean distinct =false;

    private List<String> fetchProps;

    public void addFetchProps(String props) {
        if (fetchProps == null) {
            fetchProps = new ArrayList<>();
        }
        fetchProps.add(props);
    }

    public List<String> getFetchProps() {
        return fetchProps;
    }

    public void setFetchProps(List<String> fetchProps) {
        this.fetchProps = fetchProps;
    }

    public ParsedCount clone() {
        return Cloner.standard().deepClone(this);
    }


    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }
}
