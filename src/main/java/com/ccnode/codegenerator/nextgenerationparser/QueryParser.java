package com.ccnode.codegenerator.nextgenerationparser;

import com.ccnode.codegenerator.jpaparse.KeyWordConstants;
import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryBuilder;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.count.ParsedCountDto;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.delete.ParsedDeleteDto;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.find.ParsedFindDto;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.update.ParsedUpdateDto;
import com.ccnode.codegenerator.nextgenerationparser.parser.CountParser;
import com.ccnode.codegenerator.nextgenerationparser.parser.DeleteParser;
import com.ccnode.codegenerator.nextgenerationparser.parser.FindParser;
import com.ccnode.codegenerator.nextgenerationparser.parser.UpdateParser;
import com.ccnode.codegenerator.pojo.MethodXmlPsiInfo;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryParser {

    public static QueryParseDto parse(List<String> props, MethodXmlPsiInfo info) {
        //make it cool to start.
        String methodLower = info.getMethodName().toLowerCase();
        if (methodLower.startsWith(KeyWordConstants.FIND)) {
            ParsedFindDto parse = new FindParser(methodLower, props).parse();
            //then build the result by it make it happen.
            return QueryBuilder.buildFindResult(parse.getParsedFinds(), parse.getParsedFindErrors(), info);
        } else if (methodLower.startsWith(KeyWordConstants.UPDATE)) {
            ParsedUpdateDto dto = new UpdateParser(methodLower, props).parse();
            //then build the result by list to control.
            return QueryBuilder.buildUpdateResult(dto.getUpdateList(), dto.getErrorList(), info);
        } else if (methodLower.startsWith(KeyWordConstants.DELETE)) {
            ParsedDeleteDto parse = new DeleteParser(methodLower, props).parse();
            return QueryBuilder.buildDeleteResult(parse.getParsedDeletes(), parse.getErrors(), info);
        } else if (methodLower.startsWith(KeyWordConstants.COUNT)) {
            //deal with it. all are just copy with find that is ok.
            ParsedCountDto parse = new CountParser(methodLower, props).parse();
            return QueryBuilder.buildCountResult(parse.getParsedCounts(), parse.getErrors(),info);
        }

        return null;
    }
}
