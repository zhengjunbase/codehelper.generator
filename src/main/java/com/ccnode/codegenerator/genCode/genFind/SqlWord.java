package com.ccnode.codegenerator.genCode.genFind;

import com.ccnode.codegenerator.pojo.PojoFieldInfo;
import groovy.sql.Sql;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/08/04 19:33
 */
public class SqlWord {
    String value;
    PojoFieldInfo fieldInfo;
    SqlWordType sqlWordType;
    SqlWord preWord;
    SqlWord nextWord;

    public PojoFieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(PojoFieldInfo fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public SqlWord getPreWord() {
        return preWord;
    }

    public void setPreWord(SqlWord preWord) {
        this.preWord = preWord;
    }

    public SqlWord getNextWord() {
        return nextWord;
    }

    public void setNextWord(SqlWord nextWord) {
        this.nextWord = nextWord;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SqlWordType getSqlWordType() {
        return sqlWordType;
    }

    public void setSqlWordType(SqlWordType sqlWordType) {
        this.sqlWordType = sqlWordType;
    }
}
