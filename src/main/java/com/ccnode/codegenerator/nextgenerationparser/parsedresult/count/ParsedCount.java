package com.ccnode.codegenerator.nextgenerationparser.parsedresult.count;

import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.ParsedBase;

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
}
