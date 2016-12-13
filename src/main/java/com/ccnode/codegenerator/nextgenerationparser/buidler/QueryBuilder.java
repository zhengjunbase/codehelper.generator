package com.ccnode.codegenerator.nextgenerationparser.buidler;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.jpaparse.KeyWordConstants;
import com.ccnode.codegenerator.nextgenerationparser.QueryParseDto;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.base.QueryRule;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.find.OrderByRule;
import com.ccnode.codegenerator.nextgenerationparser.parsedresult.find.ParsedFind;
import com.ccnode.codegenerator.pojo.MethodXmlPsiInfo;
import com.ccnode.codegenerator.util.GenCodeUtil;
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
            queryInfos.add(buildQueryInfo(find, fieldMap, info.getTableName(), pojoClass.getName()));
        }


        return new QueryParseDto();
    }

    private static QueryInfo buildQueryInfo(ParsedFind find, Map<String, String> fieldMap, String tableName, String pojoClassName) {
        QueryInfo info = new QueryInfo();
        boolean queryAllTable = false;
        boolean returnList = true;
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
                //say findById not return list.
                if (prop.toLowerCase().equals("id") && rule.getOperator() == null) {
                    returnList = false;
                }

            }
        }
        if (find.getLimit() == 1) {
            returnList = false;
        }

        if (info.getReturnClass() == null) {
            info.setReturnClass(pojoClassName);
        }

        StringBuilder builder = new StringBuilder();
        //will notice it.
        if (queryAllTable) {
            builder.append("\n\tselect <include refid=\"" + MapperConstants.ALL_COLUMN + "\"/>");
        } else {
            builder.append("\n\tselect");
            if (find.getDistinct()) {
                builder.append(" distinct(");
                for (String prop : find.getFetchProps()) {
                    builder.append(GenCodeUtil.getUnderScore(prop) + ",");
                }
                builder.deleteCharAt(builder.length() - 1);
                builder.append(")");
            } else {
                for (String prop : find.getFetchProps()) {
                    builder.append(GenCodeUtil.getUnderScore(prop) + ",");
                }
                builder.deleteCharAt(builder.length() - 1);
            }
        }
        builder.append("\n\t from " + tableName);
        if (find.getQueryRules() != null) {
            buildQuerySqlAndParam(find.getQueryRules(), info, fieldMap);
        }

        if (find.getOrderByProps() != null) {
            builder.append(" order by");
            for (OrderByRule rule : find.getOrderByProps()) {
                builder.append(" " + rule.getProp() + " " + rule.getOrder());
            }
        }
        return info;
    }

    private static void buildQuerySqlAndParam(List<QueryRule> queryRules, QueryInfo info, Map<String, String> fieldMap) {
        StringBuilder builder = new StringBuilder();
        List<ParamInfo> paramInfos = new ArrayList<>();
        for (QueryRule rule : queryRules) {
            String prop = rule.getProp();
            String operator = rule.getOperator();
            String connector = rule.getConnector();
            //mean =
            if (operator == null) {
                ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno(prop).withParamType(fieldMap.get(prop)).withParamValue(prop).build();
                paramInfos.add(paramInfo);
                builder.append(" " + prop + "=#{" + paramInfo.getParamAnno() + "}");
            } else {
                switch (operator) {
                    case KeyWordConstants.GREATERTHAN: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("min" + prop).withParamType(fieldMap.get(prop)).withParamValue("min" + prop).build();
                        paramInfos.add(paramInfo);
                        builder.append(" " + prop + cdata(">") + " #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                    case KeyWordConstants.LESSTHAN: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("max" + prop).withParamType(fieldMap.get(prop)).withParamValue("max" + prop).build();
                        paramInfos.add(paramInfo);
                        builder.append(" " + prop + cdata("<") + " #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                    case KeyWordConstants.BETWEEN: {
                        ParamInfo min = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("min" + prop).withParamType(fieldMap.get(prop)).withParamValue("min" + prop).build();
                        ParamInfo max = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("max" + prop).withParamType(fieldMap.get(prop)).withParamValue("max" + prop).build();
                        paramInfos.add(min);
                        paramInfos.add(max);
                        builder.append(" " + prop + cdata(">=") + " #{" + min.getParamAnno() + "} and " + prop + " " + cdata("<=") + " #{" + (max.getParamAnno()) + "}");
                        break;
                    }
                    case KeyWordConstants.ISNOTNULL: {
                        builder.append(" " + prop + " is not null");
                        break;
                    }
                    case KeyWordConstants.ISNULL: {
                        builder.append(" " + prop + " is null");
                        break;
                    }
                    case KeyWordConstants.NOT: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("not" + prop).withParamType(fieldMap.get(prop)).withParamValue("not" + prop).build();
                        paramInfos.add(paramInfo);
                        builder.append(" " + prop + "!= #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                    case KeyWordConstants.NOTIN: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno(prop + "list").withParamType("List<" + fieldMap.get(prop) + ">").withParamValue(prop + "list").build();
                        paramInfos.add(paramInfo);
                        builder.append(" " + prop + "not in \n\t<foreach item=\"item\" index=\"index\" collection=\"" + paramInfo.getParamAnno() + "\"\n\t" +
                                "open=\"(\" separator=\",\" close=\")\">\n\t" +
                                "#{item}\n\t" +
                                "</foreach>\n");
                        break;
                    }
                    case KeyWordConstants.IN: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno(prop + "list").withParamType("List<" + fieldMap.get(prop) + ">").withParamValue(prop + "list").build();
                        paramInfos.add(paramInfo);
                        builder.append(" " + prop + "in \n\t<foreach item=\"item\" index=\"index\" collection=\"" + paramInfo.getParamAnno() + "\"\n\t" +
                                "open=\"(\" separator=\",\" close=\")\">\n\t" +
                                "#{item}\n\t" +
                                "</foreach>\n");
                        break;
                    }
                    case KeyWordConstants.NOTLIKE: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("notlike" + prop).withParamType(fieldMap.get(prop)).withParamValue("notlike" + prop).build();
                        paramInfos.add(paramInfo);
                        builder.append(" " + prop + "not like #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }
                    case KeyWordConstants.LIKE: {
                        ParamInfo paramInfo = ParamInfo.ParamInfoBuilder.aParamInfo().withParamAnno("like" + prop).withParamType(fieldMap.get(prop)).withParamValue("like" + prop).build();
                        paramInfos.add(paramInfo);
                        builder.append(" " + prop + "like #{" + paramInfo.getParamAnno() + "}");
                        break;
                    }

                }
            }
            builder.append(" " + connector);
        }
        info.setParamInfos(paramInfos);
        info.setSql(builder.toString());
    }


    public static String cdata(String s) {
        return "<![CDATA[" + s + "]]>";
    }


}
