package com.ccnode.codegenerator.genCode.genFind;

import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojo.PojoFieldInfo;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.ListHelper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import static com.ccnode.codegenerator.util.GenCodeUtil.FIELD_SPLITTER;
import static com.ccnode.codegenerator.util.GenCodeUtil.ONE_COMMA;
import static com.ccnode.codegenerator.util.GenCodeUtil.ONE_SPACE;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/04 22:46
 */
public class ParseJpaStrService {

    public static Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

    public static String XML_EMPTY_PREFIX = "        ";

    public static ParseJpaResponse parse(String methodName, OnePojoInfo pojoInfo) {
        List<SqlWord> wordList = Lists.newArrayList();
        List<SqlWord> fieldFragmentList = Lists.newArrayList();
        for (PojoFieldInfo each : pojoInfo.getPojoFieldInfos()) {
            SqlWord word = new SqlWord();
            word.setFieldInfo(each);
            word.setValue(each.getFieldName());
            word.setSqlWordType(SqlWordType.Field);
            fieldFragmentList.add(word);
        }
        String remainStr = methodName.toLowerCase();

        while (StringUtils.isNotBlank(remainStr)) {
            SqlWord preWord = ListHelper.nullOrLastElement(wordList);
            Pair<String, SqlWord> pair = parseOneSqlWord(remainStr, fieldFragmentList, preWord);
            remainStr = pair.getLeft();
            if (preWord != null) {
                preWord.setNextWord(pair.getRight());
            }
            pair.getRight().setPreWord(preWord);
            wordList.add(pair.getRight());
        }
        for (SqlWord sqlWord : wordList) {
            System.out.println(sqlWord.getValue() + "——" + sqlWord.getSqlWordType());
        }
        ParseJpaResponse response = new ParseJpaResponse();
        response.setInputMethodName(methodName);
        response.setOnePojoInfo(pojoInfo);
        response.setBuilder(new StringBuilder());
        response.setHasBuilds(Lists.newArrayList());
        response.setUnBuilds(wordList);
        response.setTableName(GenCodeUtil.getUnderScore(pojoInfo.getPojoName()));
        buildXmlText(response);
        buildDaoText(response);
        buildServiceText(response);

        return response;

    }

    private static void buildServiceText(ParseJpaResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("public ");
        builder.append(response.getJavaReturnType());
        builder.append(" ");
        builder.append(response.getInputMethodName());
        builder.append("(");

        Integer parameterCount = 0;
        for (MethodParameter each : response.getJavaMethodParameterList()) {
            builder.append(each.getParameterType() + " " + each.getParameterName() + FIELD_SPLITTER);
            parameterCount ++;
        }
        if(parameterCount > 0){
            GenCodeUtil.recursiveRemoveEnd(builder, FIELD_SPLITTER);
        }
        builder.append(") {\n        return" + ONE_COMMA);
        builder.append(GenCodeUtil.getLowerCamel(response.getOnePojoInfo().getPojoName()) + "Dao.");
        builder.append(response.getInputMethodName() + "(");
        for (MethodParameter each : response.getJavaMethodParameterList()) {
            builder.append(each.getParameterName() + FIELD_SPLITTER);
        }
        if(parameterCount > 0){
            GenCodeUtil.recursiveRemoveEnd(builder, FIELD_SPLITTER);
        }
        response.setServiceMethodText(builder.toString());
        System.out.println(builder.toString());
    }

    private static void buildDaoText(ParseJpaResponse response) {
        StringBuilder daoBuilder = new StringBuilder();
        daoBuilder.append(response.getJavaReturnType());
        daoBuilder.append(ONE_SPACE);
        daoBuilder.append(response.getInputMethodName());
        daoBuilder.append("(");
        Integer parameterCount = 0;
        for (MethodParameter each : response.getJavaMethodParameterList()) {
            daoBuilder.append("@Param(\"" + each.getParameterName() + "\") ");
            daoBuilder.append(each.getParameterType() + " " + each.getParameterName()  + FIELD_SPLITTER);
            parameterCount ++ ;
        }
        if(parameterCount > 0){
            GenCodeUtil.recursiveRemoveEnd(daoBuilder, FIELD_SPLITTER);
        }
        daoBuilder.append(");");
        System.out.println(daoBuilder.toString());
        response.setDaoMethodText(daoBuilder.toString());
    }



    private static void buildXmlText(ParseJpaResponse response) {
        buildSelectPart(response);
        buildAllCondition(response);
        buildOrderByPart(response);
        System.out.println(response.getBuilder().toString());
        response.setXmlMethodText(response.getBuilder().toString());
    }

    private static void buildOrderByPart(ParseJpaResponse response) {
        StringBuilder builder = new StringBuilder();
        if(!response.getUnBuilds().isEmpty() &&
                response.getUnBuilds().get(0).getSqlWordType() != SqlWordType.OrderBy){
            return;
        }
        for (SqlWord sqlWord : response.getUnBuilds()) {
            if(Lists.newArrayList(
                    SqlWordType.Desc,
                    SqlWordType.Asc,
                    SqlWordType.Limit
            ).contains(sqlWord.getSqlWordType())){
                builder.append(sqlWord.getSqlWordType().name().toUpperCase());
                builder.append(ONE_SPACE);
            }else if(sqlWord.getSqlWordType() == SqlWordType.OrderBy){
                 builder.append("ORDER BY" + ONE_SPACE);
            }else if(sqlWord.getSqlWordType() == SqlWordType.Or
                    || sqlWord.getSqlWordType() == SqlWordType.And){
                builder.append(FIELD_SPLITTER);
            }else if(sqlWord.getSqlWordType() == SqlWordType.Field){
                builder.append(GenCodeUtil.getUnderScore(sqlWord.getFieldInfo().getFieldName()));
                builder.append(ONE_SPACE);
            }else if(sqlWord.getSqlWordType() == SqlWordType.Number){
                builder.append(sqlWord.getValue());
                builder.append(ONE_SPACE);
            }
            response.getHasBuilds().add(sqlWord);
        }
        response.getBuilder().append("\n");
        response.getBuilder().append(XML_EMPTY_PREFIX + builder);

    }

    public static void buildAllCondition(ParseJpaResponse request){
        while(!request.getUnBuilds().isEmpty()
                && request.getUnBuilds().get(0).getSqlWordType() != SqlWordType.OrderBy){
            buildCondition(request);
        }
    }

    public static void buildSelectPart(ParseJpaResponse response) {
        StringBuilder sqlBuilder = new StringBuilder();
        List<SqlWord> unBuilds = Lists.newArrayList();
        List<SqlWord> beforeByList = Lists.newArrayList();
        boolean beforeBy = true;
        for (SqlWord sqlWord : response.getUnBuilds()) {
            if(sqlWord.getSqlWordType() == SqlWordType.By
                    || sqlWord.getSqlWordType() == SqlWordType.OrderBy){
                beforeBy = false;
            }
            if(beforeBy){
                beforeByList.add(sqlWord);
            }else{
                unBuilds.add(sqlWord);
            }
        }
        if(beforeByList.size() == 1){
            SqlWord first = beforeByList.get(0);
            if( first.getSqlWordType() == SqlWordType.Count){
                sqlBuilder.append("SELECT COUNT(1) FROM " + response.getTableName() + ONE_SPACE);
                response.setXmlReturnType("resultMap=\"java.lang.Integer\"");
                response.setJavaReturnType("Integer");

            }else{
                sqlBuilder.append("SELECT <include refid=\"all_column\"/> FROM " + response.getTableName() + ONE_SPACE);
                response.setXmlReturnType("resultMap=\"AllColumnMap\"");
                response.setJavaReturnType(GenCodeUtil.getUpperCamel(response.getOnePojoInfo().getPojoName()));

            }
        }else{
            sqlBuilder.append("SELECT" + ONE_SPACE);
            Integer fieldCount = 0;
            for (SqlWord sqlWord : beforeByList) {
                if(sqlWord.getSqlWordType() == SqlWordType.Field){
                    sqlBuilder.append(GenCodeUtil.getUnderScore(sqlWord.getFieldInfo().getFieldName()));
                    response.setXmlReturnType("resultMap=\"java.lang." + sqlWord.getFieldInfo().getFieldClass() + "\"");
                    response.setJavaReturnType(sqlWord.getFieldInfo().getFieldClass());
                    fieldCount ++;
                }else if(sqlWord.getSqlWordType() == SqlWordType.And){
                    sqlBuilder.append(FIELD_SPLITTER);
                }
            }
            if(fieldCount > 1){
                response.setXmlReturnType("resultMap=\"AllColumnMap\"");
                response.setJavaReturnType(GenCodeUtil.getUpperCamel(response.getOnePojoInfo().getPojoName()));
            }
            GenCodeUtil.recursiveRemoveEnd(sqlBuilder, FIELD_SPLITTER);
            sqlBuilder.append(ONE_SPACE + "FROM " + response.getTableName() + ONE_SPACE);
        }
        response.getBuilder().append(XML_EMPTY_PREFIX + sqlBuilder);
        response.getHasBuilds().addAll(beforeByList);
        response.setUnBuilds(unBuilds);
        System.out.println(response.getBuilder().toString());
    }

    /**
     * condition sample: ByUserName, ByCountBetween,ByBookIn,ByUserNameLike,ByUserNameNotLike,
     * @param context
     */
    public static void buildCondition(ParseJpaResponse context){
        StringBuilder builder = context.getBuilder();
        List<SqlWord> unBuilds = Lists.newArrayList();
        List<SqlWord> oneConditionList = Lists.newArrayList();
        Integer joinerCount = 0;
        for (SqlWord sqlWord : context.getUnBuilds()) {
            if(SqlWordType.CONDITION_JOINER_SET.contains(sqlWord.getSqlWordType())){
                joinerCount ++;
            }
            if(joinerCount <= 1){
                oneConditionList.add(sqlWord);
            }else{
                unBuilds.add(sqlWord);
            }
        }
        SqlWord joiner = oneConditionList.get(0);
        SqlWord field = oneConditionList.get(1);
        SqlWord operator = null;
        SqlWord notOperator = null;
        if(oneConditionList.size() == 4){
            notOperator = oneConditionList.get(2);
            operator = oneConditionList.get(3);
        }
        if(oneConditionList.size() == 3){
            operator = oneConditionList.get(2);
        }
        if(joiner.getSqlWordType() == SqlWordType.By){
            builder.append("WHERE ");
        }else {
            builder.append(joiner.getSqlWordType().name().toUpperCase()).append(" ");
        }
        String condition = buildByOperator(context, notOperator, operator, field);
        context.getBuilder().append("\n");
        context.getBuilder().append(XML_EMPTY_PREFIX + condition);
        context.setUnBuilds(unBuilds);
        context.getHasBuilds().addAll(oneConditionList);
        System.out.println(XML_EMPTY_PREFIX + builder.toString());
    }

    public static String buildNotWord(SqlWord not){
        if(not == null){
            return ONE_SPACE;
        }else{
            return  " NOT ";
        }
    }

    private static String buildByOperator(ParseJpaResponse response, SqlWord notOperator, SqlWord joiner, SqlWord field) {
        PojoFieldInfo fieldInfo = field.getFieldInfo();
        String underScore = GenCodeUtil.getUnderScore(fieldInfo.getFieldName());
        String lowerCamel = GenCodeUtil.getLowerCamel(fieldInfo.getFieldName());
        String upperCamel = GenCodeUtil.getUpperCamel(fieldInfo.getFieldName());
        List<MethodParameter> parameterList = response.getJavaMethodParameterList();
        if(joiner == null || joiner.getSqlWordType() == SqlWordType.By){
            response.addMethodParameter(fieldInfo.getFieldClass(), lowerCamel);
            return underScore + " = "+ "#{" + lowerCamel + "} ";
        }
        SqlWordType sqlWordType = joiner.getSqlWordType();
        if(sqlWordType == SqlWordType.Like) {
            response.addMethodParameter(fieldInfo.getFieldClass(), lowerCamel);
            return underScore + buildNotWord(notOperator) + sqlWordType.name().toUpperCase() + " CONCAT('%', #{" + lowerCamel + "}, '%') ";
        }else if(sqlWordType == SqlWordType.EndWith){
            response.addMethodParameter(fieldInfo.getFieldClass(), lowerCamel);
            return underScore + buildNotWord(notOperator) + "LIKE" + " CONCAT('%', #{" + lowerCamel + "}) ";
        }else if(sqlWordType == SqlWordType.StartWith){
            response.addMethodParameter(fieldInfo.getFieldClass(), lowerCamel);
            return underScore + buildNotWord(notOperator) + "LIKE" + " CONCAT(#{" + lowerCamel + "}, '%') ";
        }else if(sqlWordType == SqlWordType.In || sqlWordType == SqlWordType.Exists){
            response.addMethodParameter("List<" + fieldInfo.getFieldClass() + ">", lowerCamel + "s");
            return underScore + buildNotWord(notOperator) + sqlWordType.name().toUpperCase() + " " + "#{" + lowerCamel + "s} ";
        }else if(sqlWordType == SqlWordType.Before || sqlWordType == SqlWordType.LessThan){
            response.addMethodParameter(fieldInfo.getFieldClass(), lowerCamel);
            return underScore + " " + "<![CDATA[ < ]]> " + "#{" + lowerCamel + "} ";
        }else if(sqlWordType == SqlWordType.After || sqlWordType == SqlWordType.GreaterThan){
            response.addMethodParameter(fieldInfo.getFieldClass(), lowerCamel);
            return underScore + " " + "<![CDATA[ > ]]> " + "#{" + lowerCamel + "} ";
        }else if(sqlWordType == SqlWordType.GreaterThanEqual){
            response.addMethodParameter(fieldInfo.getFieldClass(), lowerCamel);
            return underScore + " " + "<![CDATA[ >= ]]> " + "#{" + lowerCamel + "} ";
        }else if( sqlWordType == SqlWordType.LessThanEqual){
            response.addMethodParameter(fieldInfo.getFieldClass(), lowerCamel);
            return underScore + " " + "<![CDATA[ <= ]]> " + "#{" + lowerCamel + "} ";
        }else if( sqlWordType == SqlWordType.Between){
            response.addMethodParameter(fieldInfo.getFieldClass(), " min" + upperCamel);
            response.addMethodParameter(fieldInfo.getFieldClass(), " max" + upperCamel);
            return underScore + " " + "<![CDATA[ >= ]]> " + "#{min" + upperCamel + "}\n"
                    +XML_EMPTY_PREFIX + "AND " + underScore + " " + "<![CDATA[ < ]]> " + "#{max" + upperCamel + "} " ;
        }
        return null;
    }

    public static Pair<String, SqlWord> parseOneSqlWord(String s, List<SqlWord> fieldList, SqlWord preWord) {
        List<String> canMatchGroup = Lists.newArrayList();
        if (preWord == null) {
            canMatchGroup.add(SqlWordType.Select.name());
            canMatchGroup.add(SqlWordType.Find.name());
            canMatchGroup.add(SqlWordType.Count.name());
            canMatchGroup.add(SqlWordType.Update.name());
        } else {
            String desc = preWord.getSqlWordType().getCanFollowStr();
            canMatchGroup = COMMA_SPLITTER.splitToList(desc);
        }
        List<SqlWord> canMatchList = Lists.newArrayList();
        if (StringUtils.isNumeric(s.substring(0, 1))) {
            int i = 0;
            while (i < s.length() && StringUtils.isNumeric(s.substring(i, i + 1))) {
                i++;
            }
            SqlWord number = new SqlWord();
            number.setSqlWordType(SqlWordType.Number);
            number.setValue((s.substring(0, i)));
            return Pair.of(s.substring(number.value.length()), number);
        } else {
            for (String split : canMatchGroup) {
                if (StringUtils.containsIgnoreCase(split, SqlWordType.Field.name())) {
                    canMatchList.addAll(fieldList);
                } else {
                    SqlWord fragment = new SqlWord();
                    fragment.setSqlWordType(SqlWordType.fromNameIgnoreCase(split));
                    fragment.setValue(SqlWordType.fromNameIgnoreCase(split).name());
                    canMatchList.add(fragment);
                }
            }
            for (SqlWord each : canMatchList) {
                if (StringUtils.startsWithIgnoreCase(s, each.getValue())) {
                    return Pair.of(s.substring(each.value.length()), each);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        OnePojoInfo pojoInfo = new OnePojoInfo();
        pojoInfo.setPojoFieldInfos(Lists.newArrayList());
        PojoFieldInfo fieldInfo = new PojoFieldInfo();
        fieldInfo.setFieldName("Ape");
        pojoInfo.getPojoFieldInfos().add(fieldInfo);
        PojoFieldInfo fieldInfo1 = new PojoFieldInfo();
        fieldInfo1.setFieldName("Id");
        pojoInfo.getPojoFieldInfos().add(fieldInfo1);
        pojoInfo.setPojoName("carInfo");
         parse("findApeOrderById", pojoInfo);
    }

}
