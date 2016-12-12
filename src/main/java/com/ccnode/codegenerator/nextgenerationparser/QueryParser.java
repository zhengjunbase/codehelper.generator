package com.ccnode.codegenerator.nextgenerationparser;

import com.ccnode.codegenerator.jpaparse.KeyWordConstants;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.ParsedFindDto;
import com.ccnode.codegenerator.nextgenerationparser.parser.FindParser;
import com.intellij.psi.xml.XmlTag;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryParser {

    public static List<XmlTag> parse(String methodName,List<String> props){
        //make it cool to start.
        String methodLower = methodName.toLowerCase();
        if(methodLower.startsWith(KeyWordConstants.FIND)){
            ParsedFindDto parse = new FindParser(methodLower, props).parse();
            //then build the result by it make it happen.
        } else if(methodLower.startsWith(KeyWordConstants.UPDATE)){

        } else if(methodLower.startsWith(KeyWordConstants.DELETE)){

        } else if(methodLower.startsWith(KeyWordConstants.COUNT)){
            //deal with it. all are just copy with find that is ok.
        }

        return null;
    }
}
