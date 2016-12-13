package com.ccnode.codegenerator.nextgenerationparser.buidler;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.nextgenerationparser.QueryParseDto;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.QueryRule;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.pojo.MethodXmlPsiInfo;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryBuilder {

    public static QueryParseDto buildFindResult(List<ParsedFind> parsedFinds, MethodXmlPsiInfo info) {
        List<QueryInfo> queryInfos = new ArrayList<>();
        //get pojo class all fields and their type do it cool.
        PsiClass pojoClass = info.getPojoClass();
        Map<String, String> fieldMap = new HashMap<>();
        PsiField[] allFields = pojoClass.getAllFields();
        for (PsiField f : allFields) {
            if (f.hasModifierProperty("private") && !f.hasModifierProperty("static")) {
                fieldMap.put(f.getName(), f.getType().getCanonicalText());
            }
        }
        for (ParsedFind find : parsedFinds) {
            queryInfos.add(buildQueryInfo(find, fieldMap, info.getTableName()));
        }


        return new QueryParseDto();
    }

    private static QueryInfo buildQueryInfo(ParsedFind find, Map<String, String> fieldMap, String tableName) {
        QueryInfo info = new QueryInfo();
        boolean queryAllTable = false;
        if (find.getFetchProps() != null && find.getFetchProps().size() > 0) {
            if (find.getFetchProps().size() > 1) {
                info.setReturnMap(MapperConstants.ALL_COLUMN_MAP);
            } else {
                //说明等于1
                String s = find.getFetchProps().get(0);
                info.setReturnClass(fieldMap.get(s));
            }
        } else {
            queryAllTable = true;
            info.setReturnMap(MapperConstants.ALL_COLUMN_MAP);
        }
//later will check wether it is the same with method.
        if (find.getQueryRules() != null) {
            //will build with params.
            for (QueryRule rule : find.getQueryRules()) {
                String prop = rule.getProp();
                ParamInfo paramInfo = new ParamInfo();
                paramInfo.setParamAnno(prop);
                paramInfo.setParamType(fieldMap.get(prop));
                paramInfo.setParamValue(prop);
                info.addParams(paramInfo);
            }
        }

        StringBuilder builder = new StringBuilder();
        //will notice it.
        
        return info;
    }
}
