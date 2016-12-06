package com.ccnode.codegenerator.jpaparse.info;

/**
 * Created by bruce.ge on 2016/12/5.
 */
public class BaseInfo {
    protected Integer paramCount = 0;
    protected String lastQueryProp = "";
    protected String queryPart = "";
    protected String table;
    protected Integer lastEqualLength = 0;


    public Integer getParamCount() {
        return paramCount;
    }

    public void setParamCount(Integer paramCount) {
        this.paramCount = paramCount;
    }

    public String getLastQueryProp() {
        return lastQueryProp;
    }

    public void setLastQueryProp(String lastQueryProp) {
        this.lastQueryProp = lastQueryProp;
    }

    public String getQueryPart() {
        return queryPart;
    }

    public void setQueryPart(String queryPart) {
        this.queryPart = queryPart;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getLastEqualLength() {
        return lastEqualLength;
    }

    public void setLastEqualLength(Integer lastEqualLength) {
        this.lastEqualLength = lastEqualLength;
    }
}
