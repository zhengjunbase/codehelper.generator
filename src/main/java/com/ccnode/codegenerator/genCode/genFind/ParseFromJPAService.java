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

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/04 22:46
 */
public class ParseFromJpaService {

    public static Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

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
        builder.append(response.getJavaReturnType());
        builder.append(" ");
        builder.append(response.getInputMethodName());
        builder.append("(");

        for (MethodParameter each : response.getJavaMethodParameterList()) {
            builder.append(each.getParameterType() + " " + each.getParameterName() + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("){\n        return ");
        builder.append(GenCodeUtil.getLowerCamel(response.getOnePojoInfo().getPojoName()) + "Dao.");
        builder.append(response.getInputMethodName());
        for (MethodParameter each : response.getJavaMethodParameterList()) {
            builder.append(each.getParameterName() + ", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append(");\n    }");
        response.setServiceText(builder.toString());
        System.out.println(builder.toString());
    }

    private static void buildDaoText(ParseJpaResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append(response.getJavaReturnType());
        builder.append(" ");
        builder.append(response.getInputMethodName());
        builder.append("(");

        for (MethodParameter each : response.getJavaMethodParameterList()) {
            builder.append("@Param(\"" + each.getParameterName() + "\") ");
            builder.append(each.getParameterType() + " " + each.getParameterName() + ",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(");");
        System.out.println(builder.toString());
        response.setDaoText(builder.toString());
    }



    private static void buildXmlText(ParseJpaResponse response) {
        buildXmlText(response);
        buildSelectPart(response);
        buildAllCondition(response);
        buildOrderByPart(response);
        System.out.println(response.getBuilder().toString());
        response.setXmlText(response.getBuilder().toString());
    }

    private static void buildOrderByPart(ParseJpaResponse request) {
        StringBuilder builder = request.getBuilder();
        if(!request.getUnBuilds().isEmpty() &&
                request.getUnBuilds().get(0).getSqlWordType() != SqlWordType.OrderBy){
            return;
        }
        for (SqlWord sqlWord : request.getUnBuilds()) {
            if(Lists.newArrayList(
                    SqlWordType.Desc,
                    SqlWordType.Asc,
                    SqlWordType.Limit
            ).contains(sqlWord.getSqlWordType())){
                builder.append(sqlWord.getSqlWordType().name().toUpperCase());
                builder.append(" ");
            }else if(sqlWord.getSqlWordType() == SqlWordType.OrderBy){
                 builder.append("ORDER BY ");
            }else if(sqlWord.getSqlWordType() == SqlWordType.Or
                    || sqlWord.getSqlWordType() == SqlWordType.And){
                builder.append(", ");
            }else if(sqlWord.getSqlWordType() == SqlWordType.Field){
                builder.append(GenCodeUtil.getUnderScore(sqlWord.getFieldInfo().getFieldName()));
                builder.append(" ");
            }else if(sqlWord.getSqlWordType() == SqlWordType.Number){
                builder.append(sqlWord.getValue());
                builder.append(" ");
            }
            request.getHasBuilds().add(sqlWord);
        }
    }

    public static void buildAllCondition(ParseJpaResponse request){
        while(!request.getUnBuilds().isEmpty()
                && request.getUnBuilds().get(0).getSqlWordType() != SqlWordType.OrderBy){
            buildCondition(request);
        }
    }

    public static void buildSelectPart(ParseJpaResponse response) {
        StringBuilder builder = response.getBuilder();
        List<SqlWord> unBuilds = Lists.newArrayList();
        List<SqlWord> beforeByList = Lists.newArrayList();
        boolean beforeBy = true;
        for (SqlWord sqlWord : response.getUnBuilds()) {
            if(sqlWord.getSqlWordType() == SqlWordType.By){
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
                builder.append("SELECT COUNT(1) FROM " + response.getTableName() + " ");
                response.setXmlReturnType("resultMap=\"java.lang.Integer\"");
                response.setJavaReturnType("Integer");

            }else{
                builder.append("SELECT <include refid=\"all_column\"/> FROM " + response.getTableName() + " ");
                response.setXmlReturnType("resultMap=\"AllColumnMap\"");
                response.setJavaReturnType(GenCodeUtil.getUpperCamel(response.getOnePojoInfo().getPojoName()));

            }
        }else{
            builder.append("SELECT ");
            Integer fieldCount = 0;
            for (SqlWord sqlWord : beforeByList) {
                if(sqlWord.getSqlWordType() == SqlWordType.Field){
                    builder.append(GenCodeUtil.getUnderScore(sqlWord.getFieldInfo().getFieldName()));
                    response.setXmlReturnType("resultMap=\"java.lang." + sqlWord.getFieldInfo().getFieldClass() + "\"");
                    response.setJavaReturnType(sqlWord.getFieldInfo().getFieldClass());
                    fieldCount ++;
                }else if(sqlWord.getSqlWordType() == SqlWordType.And){
                    builder.append(", ");
                }
            }
            if(fieldCount > 1){
                response.setXmlReturnType("resultMap=\"AllColumnMap\"");
                response.setJavaReturnType(GenCodeUtil.getUpperCamel(response.getOnePojoInfo().getPojoName()));
            }
            builder.append(" FROM " + response.getTableName() + " ");
        }
        builder.append("\n");
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

        context.getBuilder().append(condition);
        context.getBuilder().append("\n");
        context.setUnBuilds(unBuilds);
        context.getHasBuilds().addAll(oneConditionList);
        System.out.println(builder.toString());
    }

    public static String buildNotWord(SqlWord not){
        if(not == null){
            return " ";
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
            return underScore + " " + "<![CDATA[ >= ]]> " + "#{min" + upperCamel + "} "
                    + "AND " + underScore + " " + "<![CDATA[ < ]]> " + "#{max" + upperCamel + "} " ;
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
//        parse("CountByUserNameOrTypeBetweenOrFuckInOrShaBiOrFuckNotLikeOrIdInOrIdNotStartWith", "CarInfo","userName", "type", "fuck",
//                "ShaBi", "Id");
        OnePojoInfo pojoInfo = new OnePojoInfo();
        pojoInfo.setPojoFieldInfos(Lists.newArrayList());
        PojoFieldInfo fieldInfo = new PojoFieldInfo();
        fieldInfo.setFieldName("userName");
        pojoInfo.getPojoFieldInfos().add(fieldInfo);
        pojoInfo.setPojoName("carInfo");
//         parse("findByUserNameAndUserNameInAndUserNameExists", pojoInfo);
    }

}
