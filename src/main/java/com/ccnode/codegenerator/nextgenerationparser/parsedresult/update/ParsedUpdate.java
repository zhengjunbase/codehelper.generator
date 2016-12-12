package com.ccnode.codegenerator.nextgenerationparser.parsedresult.update;

import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.ParsedBase;
import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedUpdate extends ParsedBase {
    private List<String> updateProps;

    public void addUpdateProps(String prop) {
        if (updateProps == null) {
            updateProps = new ArrayList<>();
        }
        updateProps.add(prop);
    }

    public ParsedUpdate clone() {
        return Cloner.standard().deepClone(this);
    }

}
