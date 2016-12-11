package com.ccnode.codegenerator.xml;

import com.intellij.psi.xml.XmlTag;

/**
 * Created by bruce.ge on 2016/12/11.
 */
@Deprecated
public class XmlTagAndValue {
    private XmlTag tag;

    private String value;

    public XmlTag getTag() {
        return tag;
    }

    public void setTag(XmlTag tag) {
        this.tag = tag;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
