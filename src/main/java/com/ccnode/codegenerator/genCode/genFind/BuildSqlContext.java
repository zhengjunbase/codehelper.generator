package com.ccnode.codegenerator.genCode.genFind;

import java.util.List;
import java.util.zip.ZipEntry;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/05 12:39
 */
public class BuildSqlContext {
    String tableName;
    List<SqlWord> sqlWordList;
    StringBuilder builder;
    String javaReturnType;
    List<String> javaMethodParameterList;

    public String getJavaReturnType() {
        return javaReturnType;
    }

    public void setJavaReturnType(String javaReturnType) {
        this.javaReturnType = javaReturnType;
    }

    public List<String> getJavaMethodParameterList() {
        return javaMethodParameterList;
    }

    public void setJavaMethodParameterList(List<String> javaMethodParameterList) {
        this.javaMethodParameterList = javaMethodParameterList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<SqlWord> getSqlWordList() {
        return sqlWordList;
    }

    public void setSqlWordList(List<SqlWord> sqlWordList) {
        this.sqlWordList = sqlWordList;
    }

    public StringBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(StringBuilder builder) {
        this.builder = builder;
    }
}
