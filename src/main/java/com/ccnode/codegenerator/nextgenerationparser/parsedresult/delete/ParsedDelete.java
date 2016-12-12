package com.ccnode.codegenerator.nextgenerationparser.parsedresult.delete;

import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.ParsedBase;
import com.rits.cloning.Cloner;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class ParsedDelete extends ParsedBase {
    public ParsedDelete clone() {
        return Cloner.standard().deepClone(this);
    }
}
