package com.ccnode.codegenerator.pojo;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/06/09 08:49
 */
public class DaoParam {

    private static final String EMPTY = "";
    String limit = EMPTY;
    String queryCondition = EMPTY;
    String updateCondition = EMPTY;
    String returnCount = EMPTY;

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getQueryCondition() {
        return queryCondition;
    }

    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    public String getUpdateCondition() {
        return updateCondition;
    }

    public void setUpdateCondition(String updateCondition) {
        this.updateCondition = updateCondition;
    }

    public String getReturnCount() {
        return returnCount;
    }

    public void setReturnCount(String returnCount) {
        this.returnCount = returnCount;
    }
}
