package com.ccnode.codegenerator.nextgenerationparser;

import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryInfo;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryParseDto {
    private List<QueryInfo> queryInfos;

    private Boolean hasMatched = false;


    public List<QueryInfo> getQueryInfos() {
        return queryInfos;
    }

    public void setQueryInfos(List<QueryInfo> queryInfos) {
        this.queryInfos = queryInfos;
    }

    private List<String> errorMsg;

    public Boolean getHasMatched() {
        return hasMatched;
    }

    public void setHasMatched(Boolean hasMatched) {
        this.hasMatched = hasMatched;
    }

    public List<String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(List<String> errorMsg) {
        this.errorMsg = errorMsg;
    }
}
