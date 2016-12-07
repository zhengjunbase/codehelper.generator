package com.ccnode.codegenerator.jpaparse.info;

/**
 * Created by bruce.ge on 2016/12/4.
 */
public class FindInfo extends BaseInfo {

    private Boolean distinct = false;

    private Boolean allField = true;

    private String fetchPart = "";


    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    public Boolean getAllField() {
        return allField;
    }

    public void setAllField(Boolean allField) {
        this.allField = allField;
    }

    public String getFetchPart() {
        return fetchPart;
    }

    public void setFetchPart(String fetchPart) {
        this.fetchPart = fetchPart;
    }
}
