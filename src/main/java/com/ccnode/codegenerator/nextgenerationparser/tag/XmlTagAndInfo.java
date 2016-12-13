package com.ccnode.codegenerator.nextgenerationparser.tag;

import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryInfo;
import com.intellij.psi.xml.XmlTag;

/**
 * Created by bruce.ge on 2016/12/13.
 */
public class XmlTagAndInfo {
    private XmlTag xmlTag;

    private QueryInfo info;

    public QueryInfo getInfo() {
        return info;
    }

    public void setInfo(QueryInfo info) {
        this.info = info;
    }

    public XmlTag getXmlTag() {
        return xmlTag;
    }

    public void setXmlTag(XmlTag xmlTag) {
        this.xmlTag = xmlTag;
    }
}
