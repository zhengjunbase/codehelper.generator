package com.ccnode.codegenerator.util;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.IOUtils.writeLines;

/**
 * What always stop you is what you always believe.
 * <p/>
 * Created by zhengjun.du on 2016/01/09 11:19
 */

/**
 * 根据 pojo 自动生成 建表SQL和mapper.xml文件（增、删、改、查）
 */
public class DaoCodeUtil {

    private static final String POJO_PACKAGE_NAME = "pojoFileName";
    private static final String POJO_TABLE_NAME = "pojoTableName";
    private static final String POJO_NAME = "pojoName";
    private static final String POJO_FILE_NAME = "pojoFileName";
    private static final String ONE_RETRACT = "    ";
    private static final String TWO_RETRACT = "        ";
    private static final String THREE_RETRACT = "            ";
    private static final String FOUR_RETRACT = "                ";
    private static String MYSQL_TYPE = StringUtils.EMPTY;
    private static String PACKAGE_LINE = StringUtils.EMPTY;

    public static void main(String[] args) throws IOException, URISyntaxException {
        GenCodeConfig genCodeConfig = new GenCodeConfig();
        genCodeConfig.setProjectPath("/Users/zhengjun/Workspaces/inspace/insurance_statistic");
        genCodeConfig.setServiceDir("/Users/zhengjun/Workspaces/inspace/insurance_statistic/src/main/java/com/qunar/insurance/statistic/risk/service");
        genCodeConfig.setDaoDir("/Users/zhengjun/Workspaces/inspace/insurance_statistic/src/main/java/com/qunar/insurance/statistic/dao");
        genCodeConfig.setMapperDir("/Users/zhengjun/Workspaces/inspace/insurance_statistic/src/main/resources/mappers");
        genCodeConfig.setSqlDir("/Users/zhengjun/Workspaces/inspace/insurance_statistic/doc/sql");

        /**
         * pojoName 设置 pojo名称
         * e.g. UserInfo
         */

        genCodeConfig.setPojoName("TicketCountDto");
        genCode(genCodeConfig);

    }

    public static void genCode(GenCodeConfig genCodeConfig) throws IOException, URISyntaxException {

        // pxc or mmm
        MYSQL_TYPE = "mmm";
        String pojoFilePath = getPojoFilePath(genCodeConfig.getProjectPath(), genCodeConfig.getPojoName());
        String pojoPackageName = getPojoPackageName(pojoFilePath);
        Map<String,String> fieldTypeMap = loadFieldMap(pojoFilePath).getLeft();
        fieldTypeMap.put(POJO_PACKAGE_NAME, pojoPackageName);
        fieldTypeMap.put(POJO_TABLE_NAME, getUnderScore(genCodeConfig.getPojoName()));

        List<String> fieldList = loadFieldMap(pojoFilePath).getRight();
        String sqlFileContent = generateSQLFile(fieldTypeMap,fieldList);
        File sqlFile = new File(genCodeConfig.getSqlDir());
        writeLines(ImmutableList.of(sqlFileContent), "\n",new FileOutputStream(sqlFile));

        System.out.println(sqlFileContent);
        String mapperFileContent = generateMapperFile(fieldTypeMap,fieldList);
        File mapperFile = new File(genCodeConfig.getMapperDir());
        writeLines(ImmutableList.of(mapperFileContent), "\n",new FileOutputStream(mapperFile));
        System.out.println(mapperFileContent);

        String daoFileContent = generateDaoFile(fieldTypeMap,fieldList);
        File daoFile = new File(genCodeConfig.getDaoDir());
        writeLines(ImmutableList.of(daoFileContent), "\n",new FileOutputStream(daoFile));
        System.out.println(daoFileContent);

        String serviceFileContent = generateServiceFile(fieldTypeMap, fieldList);
        File serviceFile = new File(genCodeConfig.getServiceDir());
        writeLines(ImmutableList.of(serviceFileContent), "\n",new FileOutputStream(serviceFile));
        System.out.println(serviceFileContent);
    }


    private static String getServiceFilePath(String pojoFilePath) {
        return pojoFilePath.substring(0,pojoFilePath.length()-5)+"Service.java";
    }

    private static String getDaoFilePath(String pojoFilePath) {
        return pojoFilePath.substring(0,pojoFilePath.length()-5)+"Dao.java";
    }


    private static String getSqlFilePath(String pojoFilePath) {
        return pojoFilePath.substring(0,pojoFilePath.length()-5)+"alter.sql";
    }

    private static String getMapperFilePath(String pojoFilePath) {
        return pojoFilePath.substring(0,pojoFilePath.length()-5)+"Dao.xml";
    }

    private static String getPojoPackageName(String pojoFilePath) {
        int comIndex = pojoFilePath.replace("/",".").indexOf("com.");
        String ret = pojoFilePath.replace("/",".").substring(comIndex);
        return ret.substring(0,ret.length() - 5);
    }

    private static String getPojoFilePath(String projectLocation, String pojoName) throws URISyntaxException {
        List<File> allSubFiles = IOUtils.getAllSubFiles(projectLocation);
        for (File file : allSubFiles) {
            if(file.getAbsolutePath().endsWith("/"+pojoName+".java")){
                return file.getAbsolutePath();
            }
        }
        return StringUtils.EMPTY;
    }

     private static String generateDaoFile(Map<String, String> fieldTypeMap, List<String> fieldList) {
         String pojoNameDao = fieldTypeMap.get("pojoName")+"Dao";
         String pojoName = fieldTypeMap.get("pojoName");
         StringBuilder ret = new StringBuilder();
         ret.append(PACKAGE_LINE);
         ret.append(generateJavaSignature());
         if(MYSQL_TYPE.equalsIgnoreCase("pxc")){
             ret.append("@PXCDao\n");
         }
         ret.append("public interface "+pojoNameDao +" extends GenericDao<"+pojoName+"> {\n")
                 .append("\n")
                 .append("}\n");
         return ret.toString();
    }

    private static String generateJavaSignature(){
        StringBuilder ret = new StringBuilder();
         ret.append("/**\n")
                 .append(" * Auto Generated on " + com.ccnode.codegenerator.util.DateUtil.formatLong(new Date()) +"\n")
                 .append(" */\n");
        return ret.toString();
    }

    private static String generateServiceFile(Map<String, String> fieldTypeMap, List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        String pojoName = fieldTypeMap.get("pojoName");
        String pojoNameDao = fieldTypeMap.get("pojoName")+"Dao";
        ret.append(PACKAGE_LINE);
        ret.append(generateJavaSignature());
        ret.append("@Service\n");
        ret.append("public class ")
                .append(pojoName).append("Service")
                .append(" extends GenericService<")
                .append(pojoName)
                .append("> {")
                .append("\n\n    @Resource\n")
                .append(ONE_RETRACT).append("private ").append(pojoName).append("Dao ").append(getLowerCamel(pojoName)).append("Dao;\n")
                .append("\n    @Override\n")
                .append(ONE_RETRACT).append("public GenericDao<").append(pojoName).append("> getGenericDao() {\n")
                .append(TWO_RETRACT).append("return ").append(getLowerCamel(pojoNameDao)).append(";\n")
                .append(ONE_RETRACT).append("}\n")
                .append("}\n");
        return ret.toString();
    }

    private static List<String> replaceMapperFile(List<String> oldFile ,Map<String,List<String>> segments){

        List<String> retList = Lists.newArrayList();
        String startKeyWord = "<resultMap id=\"BaseResultMap\" type=\"";
        String endKeyWord = "</resultMap>";
        List<String> replaceList = segments.get("resultMap");
        replaceList(startKeyWord,endKeyWord,oldFile,replaceList);

        startKeyWord = "<sql id=\"all_column\">";
        endKeyWord = "</sql>";
        replaceList = segments.get("sql");
        replaceList(startKeyWord,endKeyWord,oldFile,replaceList);


        startKeyWord = "<insert id=\"add\"";
        endKeyWord = "</insert>";
        replaceList = segments.get("add");
        replaceList(startKeyWord,endKeyWord,oldFile,replaceList);

        startKeyWord = "<insert id=\"adds\"";
        endKeyWord = "</insert>";
        replaceList = segments.get("adds");
        replaceList(startKeyWord,endKeyWord,oldFile,replaceList);


        startKeyWord = "<update id=\"update\">";
        endKeyWord = "</update>";
        replaceList = segments.get("update");
        replaceList(startKeyWord,endKeyWord,oldFile,replaceList);


        startKeyWord = "<select id=\"query\"";
        endKeyWord = "</select>";
        replaceList = segments.get("query");
        replaceList(startKeyWord,endKeyWord,oldFile,replaceList);

        startKeyWord = "<delete id=\"delete\">";
        endKeyWord = "</delete>";
        replaceList = segments.get("delete");
        return replaceList(startKeyWord,endKeyWord,oldFile,replaceList);
    }

    private static List<String> replaceList(String startKeyWord,String endKeyWord,  List<String> oldList, List<String>  replaceList){
        List<String> ret = Lists.newArrayList();
        int status = 0;
        for (String s : oldList) {

            if(status == 0 && !s.contains(startKeyWord)){
                ret.add(s);
                continue;
            }
            if(status == 0 && s.contains(startKeyWord)){
                ret.addAll(replaceList);
                status = 1;
                continue;
            }
            if(status == 1 && !s.contains(endKeyWord)){
                continue;
            }
            if(status == 1 && s.contains(endKeyWord)){
                status = 2;
                continue;
            }
            if(status == 2){
                ret.add(s);
            }
        }
        return ret;
    }

    private static String generateMapperFile(Map<String, String> fieldTypeMap, List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        String pojoName = fieldTypeMap.get("pojoName");
        String tableName = getUnderScore(pojoName);
        ret.append(getMapperHeader())
            .append("<mapper namespace=\"" + fieldTypeMap.get(POJO_PACKAGE_NAME) + "Dao\">\n")
            .append("\n    <!--auto generated Code-->\n")
            .append(genBaseResultMap(fieldTypeMap, fieldList))
            .append("\n    <!--auto generated Code-->\n")
            .append(genAllColumn(fieldList))
            .append("\n    <!--auto generated Code-->\n")
            .append(genAddMethod(fieldTypeMap, fieldList))
            .append("\n    <!--auto generated Code-->\n")
            .append(genAddsMethod(fieldTypeMap, fieldList))
            .append("\n    <!--auto generated Code-->\n")
            .append(genUpdateMethod(fieldTypeMap, fieldList))
            .append("\n    <!--auto generated Code-->\n")
//            .append(genUpdateWithLastUpdateMethod(fieldTypeMap, fieldList))
//            .append("\n    <!--auto generated Code-->\n")
            .append(genQueryMethod(fieldTypeMap, fieldList))
            .append("\n    <!--auto generated Code-->\n")

            .append(genQueryMethod(fieldTypeMap, fieldList))
            .append("\n    <!--auto generated Code-->\n")
            .append(genDeleteMethod(fieldTypeMap, fieldList))
            .append("</mapper>");
        ;

        return ret.toString();
    }

    private static String genAddMethod(Map<String, String> fieldTypeMap, List<String> fieldList) {
         StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("<insert id=\"add\"\n");
        if(MYSQL_TYPE.equalsIgnoreCase("mmm")){
            ret.append(TWO_RETRACT).append("useGeneratedKeys=\"true\"\n")
               .append(TWO_RETRACT).append("keyProperty=\"pojo.").append(fieldList.get(0)).append("\"\n");

        }
            ret.append(TWO_RETRACT).append(">\n")
                .append(TWO_RETRACT).append("INSERT INTO " + fieldTypeMap.get(POJO_TABLE_NAME) + "(\n")
                .append(TWO_RETRACT).append("<include refid=\"all_column\"/>\n").append(TWO_RETRACT).append(")VALUES\n")
                .append(THREE_RETRACT).append("(\n")
        ;
        for (String field : fieldList) {
            if(isNotField(field) || fieldList.indexOf(field) == 0){
                continue;
            }
            ret.append(THREE_RETRACT).append("#{pojo." + field + "}");
            if(fieldList.indexOf(field) != fieldList.size() - 1) {
                ret.append(",");
            }
            ret.append("\n");
        }
        ret.append(THREE_RETRACT).append(")\n")
            .append(ONE_RETRACT).append("</insert>\n");

        return ret.toString();
    }

    private static String genQueryMethod(Map<String, String> fieldTypeMap, List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("<select id=\"query\" resultMap=\"BaseResultMap\">\n")
            .append(TWO_RETRACT).append("SELECT\n")
            .append(THREE_RETRACT).append(getUnderScore(fieldList.get(0))).append(", <include refid=\"all_column\"/>\n")
            .append(TWO_RETRACT).append("FROM ").append(fieldTypeMap.get(POJO_TABLE_NAME) + "\n")
            .append(TWO_RETRACT).append("WHERE id != -1\n")
            ;
        for (String field : fieldList) {
            if(isNotField(field)){
                continue;
            }
            ret.append(TWO_RETRACT).append("<if test=\"pojo." + field+" != null\">\n")
                .append(THREE_RETRACT).append("AND "+getUnderScore(field)+" = #{pojo."+field+"}\n")
                .append(TWO_RETRACT).append("</if>\n")
            ;
        }
        ret.append(TWO_RETRACT).append("LIMIT #{withLimit}\n");
        ret.append(ONE_RETRACT).append("</select>\n");
        return ret.toString();
    }

    private static String  genAddsMethod(Map<String, String> fieldTypeMap, List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("<insert id=\"adds\">\n")
            .append(TWO_RETRACT).append("INSERT INTO " + fieldTypeMap.get("pojoTableName") + "(\n")
            .append(TWO_RETRACT).append("<include refid=\"all_column\"/>\n")
            .append(TWO_RETRACT).append(")VALUES\n")
            .append(TWO_RETRACT).append(
                "<foreach collection=\"pojos\" item=\"pojo\" index=\"index\" separator=\",\">\n")
            .append(THREE_RETRACT).append("(\n")
        ;
        for (String field : fieldList) {
            if(isNotField(field) || fieldList.indexOf(field) == 0){
                continue;
            }
            ret.append(THREE_RETRACT).append("#{pojo." + field + "}");
            if(fieldList.indexOf(field) != fieldList.size() - 1) {
                ret.append(",");
            }
            ret.append("\n");
        }
        ret.append(THREE_RETRACT).append(")\n")
            .append(TWO_RETRACT).append("</foreach>\n")
            .append(ONE_RETRACT).append("</insert>\n");

        return ret.toString();
    }


    private static String genUpdateMethod(Map<String, String> fieldTypeMap, List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("<update id=\"update\">\n")
                .append(TWO_RETRACT).append("UPDATE " + fieldTypeMap.get(POJO_TABLE_NAME) + "\n")
                .append(TWO_RETRACT).append("<set>\n");
        for (String field : fieldList) {
            if(isNotField(field) || fieldList.indexOf(field) == 0 || "lastUpdate".equalsIgnoreCase(field)){
                continue;
            }
            ret.append(THREE_RETRACT).append("<if test=\"pojo."+field+" != null\">\n");
            ret.append(FOUR_RETRACT).append(getUnderScore(field) + " = #{pojo." + field + "}");
            ret.append(",\n");
            ret.append(THREE_RETRACT).append("</if>\n");
        }
        ret.append(TWO_RETRACT).append("</set>\n").append(TWO_RETRACT).append(
                "WHERE " + getUnderScore(fieldList.get(0)) + " = #{pojo." + fieldList.get(0) + "}\n")
                .append(TWO_RETRACT).append("<if test=\"withLastUpdate == 'true'\">\n").append(THREE_RETRACT)
                .append("AND last_update = #{pojo.lastUpdate}\n").append(TWO_RETRACT).append("</if>\n")
                .append(ONE_RETRACT).append("</update>\n");

        return ret.toString();
    }

    private static String genUpdateWithLastUpdateMethod(Map<String, String> fieldTypeMap, List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("<update id=\"updateWithLastUpdate\">\n")
                .append(TWO_RETRACT).append("UPDATE " + fieldTypeMap.get(POJO_TABLE_NAME) + "\n")
                .append(TWO_RETRACT).append("<set>\n");
        for (String field : fieldList) {
            if(isNotField(field) || fieldList.indexOf(field) == 0 || "lastUpdate".equalsIgnoreCase(field)){
                continue;
            }
            if("String".equalsIgnoreCase(fieldTypeMap.get(field))){
                ret.append(THREE_RETRACT).append("<if test=\"pojo." + field + " != null and pojo." + field + "!=''\">\n");
            }else{
                ret.append(THREE_RETRACT).append("<if test=\"pojo."+field+" != null\">\n");
            }
            ret.append(FOUR_RETRACT).append(getUnderScore(field) + " = #{pojo." + field + "}");
            if(fieldList.indexOf(field) != fieldList.size() - 2) {
                ret.append(",\n");
            }else{
                ret.append("\n");
            }
            ret.append(THREE_RETRACT).append("</if>\n");
        }
        ret.append(TWO_RETRACT).append("</set>\n")
            .append(TWO_RETRACT).append(
                "WHERE " + getUnderScore(fieldList.get(0)) + " = #{pojo." + fieldList.get(0) + "} AND last_update = #{pojo.lastUpdate}\n")
            .append(ONE_RETRACT).append("</update>\n");

        return ret.toString();
    }

    private static String genDeleteMethod(Map<String, String> fieldTypeMap, List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("<delete id=\"delete\">\n")
                .append(TWO_RETRACT).append("DELETE FROM " + fieldTypeMap.get(POJO_TABLE_NAME) + "\n")
                .append(TWO_RETRACT).append("WHERE "+getUnderScore(fieldList.get(0))+
                " = #{pojo."+getUnderScore(fieldList.get(0))+"}\n")
                .append(ONE_RETRACT).append("</delete>\n");
        return ret.toString();
    }


    private static String genAllColumn(List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("<sql id=\"all_column\">\n");
        for (String field : fieldList) {
            if(isNotField(field) || fieldList.indexOf(field) == 0){
                continue;
            }
            ret.append(TWO_RETRACT).append(getUnderScore(field));
            if(fieldList.indexOf(field) != fieldList.size() - 1){
               ret.append(",");
            }
            ret.append("\n");
        }
        ret.append(ONE_RETRACT).append("</sql>\n");
        return ret.toString();

    }



    private static String genBaseResultMap(Map<String, String> fieldTypeMap, List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("<resultMap id=\"BaseResultMap\" type=\""+fieldTypeMap.get(POJO_PACKAGE_NAME)+"\">\n");
        for (String field : fieldList) {
            if(isNotField(field) ) {
                continue;
            }
            if("lastUpdate".equalsIgnoreCase(field)){
                ret.append(TWO_RETRACT).append("<result column=\"last_update\" property=\"lastUpdate\"/>\n");
                continue;
            }
            ret.append(TWO_RETRACT)
                .append("<result column=\"")
                .append(getUnderScore(field))
                .append("\" property=\"")
                .append(field)
//                .append("\" jdbcType=\"")
//                .append(getSqlType(fieldTypeMap.get(field)))
                .append("\"/>\n");
            ;
        }
        ret.append("    </resultMap>\n");
        return ret.toString();
    }

    private static String generateSQLFile(Map<String, String> fieldTypeMap,List<String> fieldList) {
        StringBuilder ret = new StringBuilder();
        String pojoName = fieldTypeMap.get(POJO_NAME);
        String tableName = getUnderScore(pojoName);
        ret.append("-- auto Generated Code \n");
        ret.append("DROP TABLE IF EXISTS `"+ tableName + "`; \n");
        ret.append("CREATE TABLE ")
                .append(tableName)
                .append("(\n");
        for (String field : fieldList) {
            if(isNotField(field)){
                continue;
            }
            if("lastUpdate".equalsIgnoreCase(field)){
                ret.append(ONE_RETRACT).append("`last_update` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',\n");
                continue;
            }

            if("createTime".equalsIgnoreCase(field)){
                ret.append(ONE_RETRACT).append(
                        "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n");
                continue;
            }

            if(fieldList.indexOf(field) == 0){
                ret.append(ONE_RETRACT).append("`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',\n");
                continue;
            }

            String fieldType = fieldTypeMap.get(field);
            String fieldSql = genfieldSql(field,fieldType);
            ret.append(fieldSql);
        }
        ret.append(ONE_RETRACT).append("PRIMARY KEY (`id`)\n")
                .append(")ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '"+tableName+"';\n");



        return ret.toString();
    }

    private static String getUnderScore(String value) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
    }

    private static String getLowerCamel(String value){
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL,value);
    }


    private static String genfieldSql(String field, String fieldType) {
        StringBuilder ret = new StringBuilder();
        ret.append(ONE_RETRACT).append("`").append(getUnderScore(field))
                .append("` ")
                .append(getSqlType(fieldType))
                .append(getSqlTypeSize(getSqlType(fieldType)))
                .append(" NOT NULL DEFAULT ")
                .append(getDefaultValue(fieldType))
                .append(" COMMENT '',\n" )
                ;



        return ret.toString();
    }

    private static String getDefaultValue(String fieldType) {
        if(fieldType.equalsIgnoreCase("int")) {
            return "-1";
        }else if(fieldType.equalsIgnoreCase("integer")){
            return "-1";
        }else if(fieldType.equalsIgnoreCase("string")){
            return "''";
        }else if(fieldType.equalsIgnoreCase("short")){
            return "-127";
        }else if(fieldType.equalsIgnoreCase("float")){
            return "0";
        }else if(fieldType.equalsIgnoreCase("bigdecimal")){
            return "0";
        }else if(fieldType.equalsIgnoreCase("Long")){
            return "-1";
        }else if(fieldType.equalsIgnoreCase("Date")){
            return "'1000-01-01 00:00:00'";
        }
        throw new NullPointerException();
    }

    private static String getSqlType(String fieldType) {
        if(fieldType.equalsIgnoreCase("int")){
            return "INTEGER";
        }
        if(fieldType.equalsIgnoreCase("integer")){
            return "INTEGER";
        }else if(fieldType.equalsIgnoreCase("string")){
            return "VARCHAR";
        }else if(fieldType.equalsIgnoreCase("short")){
            return "TINYINT";
        }else if(fieldType.equalsIgnoreCase("bigdecimal")){
            return "DECIMAL";
        }else if(fieldType.equalsIgnoreCase("Float")){
            return "DECIMAL";
        }else if(fieldType.equalsIgnoreCase("Long")){
            return "BIGINT";
        }else if(fieldType.equalsIgnoreCase("Date")){
            return "DATE";
        }
        throw new NullPointerException();
    }

    private static String getSqlTypeSize(String sqlType){
          if(sqlType.equalsIgnoreCase("INTEGER")){
            return "(12)";
        }else if(sqlType.equalsIgnoreCase("VARCHAR")){
            return "(50)";
        }else if(sqlType.equalsIgnoreCase("Date")){
              return "TIME";
        }else if(sqlType.equalsIgnoreCase("TINYINT")){
            return "(4)";
        }else if(sqlType.equalsIgnoreCase("DECIMAL")){
            return "(14,4)";
        }
        return StringUtils.EMPTY;
    }


    private static Pair<Map<String, String>,List<String>> loadFieldMap(String inputFileName) throws IOException {
        Iterable<String> list = FileUtil.readFile(inputFileName);
        Map<String,String> fieldTypeMap = Maps.newHashMap();
        List<String> fieldList = Lists.newArrayList();
        Iterator<String> iterator = list.iterator();
        boolean readFieldsDone = false;
        PACKAGE_LINE = iterator.next();
        while (iterator.hasNext()){
            String next = iterator.next();
            if(next.contains("public class ")){
                fieldTypeMap.put(POJO_NAME,Splitter.on(" ").splitToList(next).get(2));
            }
            if(next.contains(") {")){
                readFieldsDone = true;
            }
            if(!next.contains(";")
                || next.contains(".")
                || next.contains("{")
                || next.contains("}")
                || next.contains("*")
//                || next.contains("/")
                || next.contains("return ")
                || next.contains("this ")
                || next.contains("serialVersionUID ")
                || StringUtils.isBlank(next)
                    ){
                continue;
            }
            if(readFieldsDone && next.contains("=")){
                continue;
            }
            next = next.replace(";","");
            List<String> strings = Splitter.on(" ").trimResults().omitEmptyStrings().splitToList(next);
            if(!(
                    strings.get(0).equals("private")
                    || strings.get(0).equals("public")
                    || strings.get(0).equals("protected")
                    )){
                strings.add(0, "private");
            }
            String field = strings.get(2);
            String fieldType = strings.get(1);
            fieldList.add(field);
            fieldTypeMap.put(field,fieldType);
        }
        return Pair.of(fieldTypeMap,fieldList);
    }

    public static String getMapperHeader() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                + "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" ;
    }

    public static boolean isNotField(String field){
        return field.equals(POJO_NAME)
                || field.equals(POJO_FILE_NAME)
                || field.equals(POJO_PACKAGE_NAME)
                || field.equals(POJO_TABLE_NAME);
    }
}
