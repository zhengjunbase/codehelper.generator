package com.ccnode.codegenerator.genCode.genFind;

import com.ccnode.codegenerator.pojo.PojoFieldInfo;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.ListHelper;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/04 22:46
 */
public class ParseFromJPAService {

    public static Splitter COMMA_SPLITER = Splitter.on(",").omitEmptyStrings().trimResults();

    public static void parse(String s, String... fieldList) {
        List<SqlWord> wordList = Lists.newArrayList();
        List<SqlWord> fieldFragmentList = Lists.newArrayList();
        for (String each : fieldList) {
            SqlWord word = new SqlWord();
            word.setValue(each);
            word.setSqlWordType(SqlWordType.Field);
            fieldFragmentList.add(word);
        }
        String remainStr = s.toLowerCase();
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
        BuildSqlContext context = new BuildSqlContext();

        buildSelectPart(context);
        buildAllCondition(context);

    }

    public static void buildAllCondition(BuildSqlContext context){
        while(!context.getSqlWordList().isEmpty()){
            buildCondition(context);
        }
    }

    public static void buildSelectPart(BuildSqlContext context) {
        StringBuilder builder = context.getBuilder();
        List<SqlWord> wordList = context.getSqlWordList();
        List<SqlWord> remainWordList = Lists.newArrayList();
        List<SqlWord> beforeByList = Lists.newArrayList();
        boolean beforeBy = true;
        for (SqlWord sqlWord : wordList) {
            if(beforeBy){
                beforeByList.add(sqlWord);
                if(sqlWord.getSqlWordType() == SqlWordType.By){
                    beforeBy = false;
                }
            }
            if(!beforeBy){
                remainWordList.add(sqlWord);
            }
        }
        if(beforeByList.size() == 1){
            builder.append("SELECT <include refid=\"all_column\"/>\n" + "        FROM " + context.getTableName());
        }else{
            builder.append("SELECT ");
            for (SqlWord sqlWord : beforeByList) {
                if(sqlWord.getSqlWordType() == SqlWordType.Field){
                    builder.append(sqlWord.getValue());
                }else if(sqlWord.getSqlWordType() == SqlWordType.And){
                    builder.append(",");
                }
            }
            builder.append(" FROM " + context.getTableName());
        }
        context.setSqlWordList(remainWordList);
    }

    /**
     * condition sample: ByUserName, ByCountBetween,ByBookIn,ByUserNameLike,ByUserNameNotLike,
     * @param context
     */
    public static void buildCondition(BuildSqlContext context){
        StringBuilder builder = context.getBuilder();
        List<SqlWord> wordList = context.getSqlWordList();
        List<SqlWord> remainWordList = Lists.newArrayList();
        List<SqlWord> oneConditionList = Lists.newArrayList();
        boolean conditionComplete = false;
        for (SqlWord sqlWord : wordList) {
            if(conditionComplete){
                oneConditionList.add(sqlWord);
                if(SqlWordType.CONDITION_JOINER_SET.contains(sqlWord.getSqlWordType())){
                    conditionComplete = true;
                }
            }
            if(!conditionComplete){
                remainWordList.add(sqlWord);
            }
        }
        SqlWord joiner = oneConditionList.get(0);
        SqlWord field = oneConditionList.get(1);
        SqlWord operator = ListHelper.nullOrLastElement(oneConditionList);
        SqlWord notOperator = null;
        if(oneConditionList.size() == 4){
            notOperator = oneConditionList.get(3);
        }
        if(joiner.getSqlWordType() == SqlWordType.By){
            builder.append("WHERE ");
        }else {
            builder.append(joiner.getSqlWordType().name().toUpperCase()).append(" ");
        }
        String condition = buildByOperator(context, notOperator, operator, field);
        context.getBuilder().append(condition);
        context.setSqlWordList(remainWordList);
        System.out.println(builder.toString());

    }

    public static String buildNotWord(SqlWord not){
        if(not == null){
            return " ";
        }else{
            return  " NOT ";
        }
    }

    private static String buildByOperator(BuildSqlContext context, SqlWord notOperator, SqlWord joiner, SqlWord field) {
        PojoFieldInfo fieldInfo = field.getFieldInfo();
        String underScore = GenCodeUtil.getUnderScore(fieldInfo.getFieldName());
        String lowerCamel = GenCodeUtil.getLowerCamel(fieldInfo.getFieldName());
        String upperCamel = GenCodeUtil.getUpperCamel(fieldInfo.getFieldName());
        List<String> parameterList = context.getJavaMethodParameterList();
        if(joiner == null){
            parameterList.add(fieldInfo.getFieldClass() + " " + lowerCamel);
            return underScore + " = "+ "#{" + underScore + "} ";
        }
        SqlWordType sqlWordType = joiner.getSqlWordType();
        if(sqlWordType == SqlWordType.Like) {
            parameterList.add(fieldInfo.getFieldClass() + " " + lowerCamel);
            return underScore + buildNotWord(notOperator) + sqlWordType.name().toUpperCase() + " CONCAT('%', #{" + lowerCamel + "}, '%')";
        }else if(sqlWordType == SqlWordType.EndWith){
            parameterList.add(fieldInfo.getFieldClass() + " " + lowerCamel);
            return underScore + buildNotWord(notOperator) + sqlWordType.name().toUpperCase() + " CONCAT('%', #{" + lowerCamel + "})";
        }else if(sqlWordType == SqlWordType.StartWith){
            parameterList.add(fieldInfo.getFieldClass() + " " + lowerCamel);
            return underScore + buildNotWord(notOperator) + sqlWordType.name().toUpperCase() + " CONCAT( #{" + lowerCamel + "}, '%')";
        }else if(sqlWordType == SqlWordType.In){
            parameterList.add("List<" + fieldInfo.getFieldClass() + "> " + lowerCamel + "s");
            return underScore + buildNotWord(notOperator) + sqlWordType.name().toUpperCase() + " " + "#{" + lowerCamel + "s} ";
        }else if(sqlWordType == SqlWordType.Before || sqlWordType == SqlWordType.LessThan){
            parameterList.add(fieldInfo.getFieldClass() + " " + lowerCamel);
            return underScore + " " + "<![CDATA[ < ]]> " + "#{" + lowerCamel + "} ";
        }else if(sqlWordType == SqlWordType.After || sqlWordType == SqlWordType.GreaterThan){
            parameterList.add(fieldInfo.getFieldClass() + " " + lowerCamel);
            return underScore + " " + "<![CDATA[ > ]]> " + "#{" + lowerCamel + "} ";
        }else if(sqlWordType == SqlWordType.GreaterThanEqual){
            parameterList.add(fieldInfo.getFieldClass() + " " + lowerCamel);
            return underScore + " " + "<![CDATA[ >= ]]> " + "#{" + lowerCamel + "} ";
        }else if( sqlWordType == SqlWordType.LessThanEqual){
            parameterList.add(fieldInfo.getFieldClass() + " " + lowerCamel);
            return underScore + " " + "<![CDATA[ <= ]]> " + "#{" + lowerCamel + "} ";
        }else if( sqlWordType == SqlWordType.Between){
            parameterList.add(fieldInfo.getFieldClass() + " min" + upperCamel);
            parameterList.add(fieldInfo.getFieldClass() + " max" + upperCamel);
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
            String desc = preWord.getSqlWordType().getDesc();
            canMatchGroup = COMMA_SPLITER.splitToList(desc);
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
        parse("findTop300ByUserNameAndTypeBetweenAndFuckInAndShaBiAndFUCKNotLikeOrIdIn", "userName", "type", "FUCK",
                "ShaBi", "Id");
    }

}
