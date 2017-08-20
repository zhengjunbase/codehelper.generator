package com.ccnode.codegenerator.genCode.genFind;

import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/05 12:39
 */
public class ParseJpaResponse {
    OnePojoInfo onePojoInfo;
    String inputMethodName;
    String tableName;
    List<SqlWord> sqlWordList;
    List<SqlWord> hasBuilds;
    List<SqlWord> unBuilds;
    StringBuilder builder;
    String javaReturnType;
    String xmlReturnType;
    List<MethodParameter> javaMethodParameterList = Lists.newArrayList();
    String daoText;
    String serviceText;
    String xmlText;

    public void addMethodParameter(String parameterType, String parameterName){
        MethodParameter methodParameter = new MethodParameter();
        methodParameter.setParameterName(parameterName);
        methodParameter.setParameterType(parameterType);
        javaMethodParameterList.add(methodParameter);
    }

    public String getInputMethodName() {
        return inputMethodName;
    }

    public void setInputMethodName(String inputMethodName) {
        this.inputMethodName = inputMethodName;
    }

    public OnePojoInfo getOnePojoInfo() {
        return onePojoInfo;
    }

    public void setOnePojoInfo(OnePojoInfo onePojoInfo) {
        this.onePojoInfo = onePojoInfo;
    }

    public String getDaoText() {
        return daoText;
    }

    public void setDaoText(String daoText) {
        this.daoText = daoText;
    }

    public String getServiceText() {
        return serviceText;
    }

    public void setServiceText(String serviceText) {
        this.serviceText = serviceText;
    }

    public String getXmlText() {
        return xmlText;
    }

    public void setXmlText(String xmlText) {
        this.xmlText = xmlText;
    }

    public String getXmlReturnType() {
        return xmlReturnType;
    }

    public void setXmlReturnType(String xmlReturnType) {
        this.xmlReturnType = xmlReturnType;
    }

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

    public List<MethodParameter> getJavaMethodParameterList() {
        return javaMethodParameterList;
    }

    public void setJavaMethodParameterList(List<MethodParameter> javaMethodParameterList) {
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
