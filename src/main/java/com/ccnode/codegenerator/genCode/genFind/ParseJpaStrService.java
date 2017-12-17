package com.ccnode.codegenerator.genCode.genFind;

import com.ccnode.codegenerator.enums.SupportFieldClass;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojo.PojoFieldInfo;
import com.ccnode.codegenerator.pojo.TextBuilder;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.ListHelper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ccnode.codegenerator.util.GenCodeUtil.*;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/04 22:46
 */
public class ParseJpaStrService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ParseJpaStrService.class);

    public static Splitter COMMA_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

    public static String XML_EMPTY_PREFIX = "        ";

    public static ParseJpaContext parse(String methodName, OnePojoInfo pojoInfo) {
        long startTime = System.currentTimeMillis();
        ParseJpaContext context = new ParseJpaContext();
        try {
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
                if(pair == null){
                    return context.failure("Failure: Invalid Input Method Name");
                }
                remainStr = pair.getLeft(); // todo bug
                if (preWord != null) {
                    preWord.setNextWord(pair.getRight());
                }
                pair.getRight().setPreWord(preWord);
                wordList.add(pair.getRight());
            }
            for (SqlWord sqlWord : wordList) {
                LOGGER.info(sqlWord.getValue() + "——" + sqlWord.getSqlWordType());
            }
            context.setInputMethodName(methodName);
            context.setOnePojoInfo(pojoInfo);
            context.setBuilder(new StringBuilder());
            context.setHasBuilds(Lists.newArrayList());
            context.setUnBuilds(wordList);
            context.setTableName(GenCodeUtil.getUnderScore(pojoInfo.getPojoName()));
            buildXmlText(context);
            buildDaoText(context);
            buildServiceText(context);
            return context;
        } catch (Throwable e) {
            LOGGER.error("ParseJpaStrService parse error, {}", methodName, e);
            return context.failure(e.getMessage());
        } finally {
            LOGGER.info("ParseJpaStrService parse cost :{}", System.currentTimeMillis() - startTime);
        }


    }

    private static void buildServiceText(ParseJpaContext context) {
        StringBuilder builder = new StringBuilder();
        builder.append("public ");
        builder.append(context.getJavaReturnType());
        builder.append(" ");
        builder.append(context.getInputMethodName());
        builder.append("(");

        Integer parameterCount = 0;
        for (MethodParameter each : context.getJavaMethodParameterList()) {
            builder.append(each.getParameterType() + " " + each.getParameterName() + FIELD_SPLITTER);
            parameterCount ++;
        }
        if(parameterCount > 0){
            GenCodeUtil.recursiveRemoveEnd(builder, FIELD_SPLITTER);
        }
        builder.append(") {\n");
        builder.append(ONE_RETRACT + "return " + GenCodeUtil.getLowerCamel(context.getOnePojoInfo().getPojoName()) + "Dao.");
        builder.append(context.getInputMethodName() + "(");
        for (MethodParameter each : context.getJavaMethodParameterList()) {
            builder.append(each.getParameterName() + FIELD_SPLITTER);
        }
        if(parameterCount > 0){
            GenCodeUtil.recursiveRemoveEnd(builder, FIELD_SPLITTER);
        }
        builder.append(");\n");
        builder.append("}\n");
        context.setServiceMethodText(builder.toString());
    }

    private static void buildDaoText(ParseJpaContext context) {
        StringBuilder daoBuilder = new StringBuilder();
        daoBuilder.append(context.getJavaReturnType());
        daoBuilder.append(ONE_SPACE);
        daoBuilder.append(context.getInputMethodName());
        daoBuilder.append("(");
        Integer parameterCount = 0;
        for (MethodParameter each : context.getJavaMethodParameterList()) {
            daoBuilder.append("@Param(\"" + each.getParameterName() + "\") ");
            daoBuilder.append(each.getParameterType() + " " + each.getParameterName()  + FIELD_SPLITTER);
            parameterCount ++ ;
        }
        if(parameterCount > 0){
            GenCodeUtil.recursiveRemoveEnd(daoBuilder, FIELD_SPLITTER);
        }
        daoBuilder.append(");");
        context.setDaoMethodText(daoBuilder.toString());
    }



    private static void buildXmlText(ParseJpaContext context) {
        TextBuilder textBuilder = new TextBuilder();
        context.setTextBuilder(textBuilder);
        buildSelectPart(context);
        buildAllCondition(context);
        buildOrderByPart(context);
        textBuilder.addRetract(ONE_RETRACT);
        String head = "<select id=\"" + context.getInputMethodName() +"\" resultMap=\"AllColumnMap\">";
        textBuilder.appendLine(0, head);
        textBuilder.appendLine("</select>");
        textBuilder.addRetract(ONE_RETRACT);
        textBuilder.appendLine("");
        context.setXmlMethodText(context.getTextBuilder().toString());
    }

    private static void buildOrderByPart(ParseJpaContext context) {
        TextBuilder builder = context.getTextBuilder();
        if(context.getUnBuilds().isEmpty() ||
                context.getUnBuilds().get(0).getSqlWordType() != SqlWordType.OrderBy){
            return;
        }
        builder.startNewLine();
        for (SqlWord sqlWord : context.getUnBuilds()) {
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
            context.getHasBuilds().add(sqlWord);
        }
    }

    public static void buildAllCondition(ParseJpaContext context){
        while(!context.getUnBuilds().isEmpty()
                && context.getUnBuilds().get(0).getSqlWordType() != SqlWordType.OrderBy){
            buildCondition(context);
        }
    }

    public static void buildSelectPart(ParseJpaContext context) {
        TextBuilder sqlBuilder = context.getTextBuilder();
        List<SqlWord> unBuilds = Lists.newArrayList();
        List<SqlWord> beforeByList = Lists.newArrayList();
        boolean beforeBy = true;
        for (SqlWord sqlWord : context.getUnBuilds()) {
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
                sqlBuilder.appendLine("SELECT COUNT(1) FROM " + context.getTableName());
                context.setXmlReturnType("resultMap=\"java.lang.Integer\"");
                context.setJavaReturnType("Integer");

            }else{
                sqlBuilder.appendLine("SELECT");
                sqlBuilder.appendLine("<include refid=\"all_column\"/>");
                sqlBuilder.appendLine("FROM " + context.getTableName() + ONE_SPACE);
                context.setXmlReturnType("resultMap=\"AllColumnMap\"");
                context.setJavaReturnType(GenCodeUtil.getUpperCamel(context.getOnePojoInfo().getPojoName()));

            }
        }else{
            sqlBuilder.appendLine("SELECT");
            Integer fieldCount = 0;
            StringBuilder fields = new StringBuilder();
            for (SqlWord sqlWord : beforeByList) {
                if(sqlWord.getSqlWordType() == SqlWordType.Field){
                    fields.append(GenCodeUtil.getUnderScore(sqlWord.getFieldInfo().getFieldName()));
                    context.setXmlReturnType("resultMap=\"java.lang." + sqlWord.getFieldInfo().getFieldClass() + "\"");
                    context.setJavaReturnType(sqlWord.getFieldInfo().getFieldClass().getPresentableText());
                    fieldCount ++;
                }else if(sqlWord.getSqlWordType() == SqlWordType.And){
                    fields.append(FIELD_SPLITTER);
                }
            }
            if(fieldCount > 1){
                context.setXmlReturnType("resultMap=\"AllColumnMap\"");
                context.setJavaReturnType(GenCodeUtil.getUpperCamel(context.getOnePojoInfo().getPojoName()));
            }
            GenCodeUtil.recursiveRemoveEnd(fields, FIELD_SPLITTER);
            sqlBuilder.appendLine(fields.toString());
            sqlBuilder.appendLine("FROM " + context.getTableName());
        }
        context.getHasBuilds().addAll(beforeByList);
        context.setUnBuilds(unBuilds);
    }

    /**
     * condition sample: ByUserName, ByCountBetween,ByBookIn,ByUserNameLike,ByUserNameNotLike,
     * @param context
     */
    public static void buildCondition(ParseJpaContext context){
        TextBuilder builder = context.getTextBuilder();
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
        builder.startNewLine();
        if(joiner.getSqlWordType() == SqlWordType.By){
            builder.append("WHERE ");
        }else {
            builder.append(joiner.getSqlWordType().name().toUpperCase());
        }
        buildByOperator(context, notOperator, operator, field);
        context.setUnBuilds(unBuilds);
        context.getHasBuilds().addAll(oneConditionList);
    }

    public static String buildNotWord(SqlWord not){
        if(not == null){
            return ONE_SPACE;
        }else{
            return  " NOT ";
        }
    }

    private static void buildByOperator(ParseJpaContext context, SqlWord notOperator, SqlWord joiner, SqlWord field) {
        TextBuilder builder = context.getTextBuilder();
        PojoFieldInfo fieldInfo = field.getFieldInfo();
        String underScore = GenCodeUtil.getUnderScore(fieldInfo.getFieldName());
        String lowerCamel = GenCodeUtil.getLowerCamel(fieldInfo.getFieldName());
        String upperCamel = GenCodeUtil.getUpperCamel(fieldInfo.getFieldName());
        if(joiner == null || joiner.getSqlWordType() == SqlWordType.By){
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), lowerCamel);
            builder.append(underScore + " = "+ "#{" + lowerCamel + "} ");
        }
        SqlWordType sqlWordType = joiner.getSqlWordType();
        if(sqlWordType == SqlWordType.Like) {
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), lowerCamel);
            builder.append( underScore + buildNotWord(notOperator) + sqlWordType.name().toUpperCase() + " CONCAT('%', #{" + lowerCamel + "}, '%') ");
        }else if(sqlWordType == SqlWordType.EndWith){
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), lowerCamel);
            builder.append( underScore + buildNotWord(notOperator) + "LIKE" + " CONCAT('%', #{" + lowerCamel + "}) ");
        }else if(sqlWordType == SqlWordType.StartWith){
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), lowerCamel);
            builder.append( underScore + buildNotWord(notOperator) + "LIKE" + " CONCAT(#{" + lowerCamel + "}, '%') ");
        }else if(sqlWordType == SqlWordType.In || sqlWordType == SqlWordType.Exists){
            context.addMethodParameter("List<" + GenCodeUtil.upperStartChar(fieldInfo.getFieldClass().getPresentableText()) + ">", lowerCamel + "s");
            builder.append(underScore + buildNotWord(notOperator) + sqlWordType.name().toUpperCase());
            builder.appendLine("<foreach item=\"item\" index=\"index\" collection=\""+ lowerCamel +"\"");
            builder.appendLine(ONE_RETRACT + "open=\"(\" separator=\",\" close=\")\">");
            builder.appendLine(TWO_RETRACT + "#{item}");
            builder.appendLine("</foreach>");
        }else if(sqlWordType == SqlWordType.Before || sqlWordType == SqlWordType.LessThan){
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), lowerCamel);
            builder.append( underScore + " " + "<![CDATA[ < ]]> " + "#{" + lowerCamel + "} ");
        }else if(sqlWordType == SqlWordType.After || sqlWordType == SqlWordType.GreaterThan){
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), lowerCamel);
            builder.append( underScore + " " + "<![CDATA[ > ]]> " + "#{" + lowerCamel + "} ");
        }else if(sqlWordType == SqlWordType.GreaterThanEqual){
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), lowerCamel);
            builder.append( underScore + " " + "<![CDATA[ >= ]]> " + "#{" + lowerCamel + "} ");
        }else if( sqlWordType == SqlWordType.LessThanEqual){
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), lowerCamel);
            builder.append( underScore + " " + "<![CDATA[ <= ]]> " + "#{" + lowerCamel + "} ");
        }else if( sqlWordType == SqlWordType.Between){
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), " min" + upperCamel);
            context.addMethodParameter(fieldInfo.getFieldClass().getPresentableText(), " max" + upperCamel);
            builder.append( underScore + " " + "<![CDATA[ >= ]]> " + "#{min" + upperCamel + "}");
            builder.appendLine(XML_EMPTY_PREFIX + "AND " + underScore + " " + "<![CDATA[ < ]]> " + "#{max" + upperCamel + "} ");
        }
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
        fieldInfo.setFieldClass(SupportFieldClass.STRING);
        pojoInfo.getPojoFieldInfos().add(fieldInfo);
        PojoFieldInfo fieldInfo1 = new PojoFieldInfo();
        fieldInfo1.setFieldName("Id");
        fieldInfo1.setFieldClass(SupportFieldClass.LONG);
        pojoInfo.getPojoFieldInfos().add(fieldInfo1);
        pojoInfo.setPojoName("carInfo");
        parse("findApeByIdOrderById", pojoInfo);
    }

}
