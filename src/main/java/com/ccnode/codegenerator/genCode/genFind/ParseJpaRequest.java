package com.ccnode.codegenerator.genCode.genFind;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/05 12:39
 */
public class ParseJpaRequest {
    String tableName;
    List<SqlWord> sqlWordList;
    List<SqlWord> hasBuilds;
    List<SqlWord> unBuilds;
    StringBuilder builder;
    String javaReturnType;
    List<String> javaMethodParameterList = Lists.newArrayList();

    public List<SqlWord> getHasBuilds() {
        return hasBuilds;
    }

    public void setHasBuilds(List<SqlWord> hasBuilds) {
        this.hasBuilds = hasBuilds;
    }

    public List<SqlWord> getUnBuilds() {
        return unBuilds;
    }

    public void setUnBuilds(List<SqlWord> unBuilds) {
        this.unBuilds = unBuilds;
    }

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
