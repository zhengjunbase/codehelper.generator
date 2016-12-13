package com.ccnode.codegenerator.nextgenerationparser;

import com.intellij.psi.xml.XmlTag;

import java.util.List;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class QueryParseDto {
    private List<XmlTag> tagList;

    private Boolean hasMatched = false;

    private List<String> errorMsg;

    public List<XmlTag> getTagList() {
        return tagList;
    }

    public void setTagList(List<XmlTag> tagList) {
        this.tagList = tagList;
    }

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
