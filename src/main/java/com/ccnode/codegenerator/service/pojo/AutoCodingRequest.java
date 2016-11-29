package com.ccnode.codegenerator.service.pojo;

import org.apache.commons.lang3.StringUtils;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/10/18 19:19
 */
public class AutoCodingRequest extends ServerRequest{

    String codingType = "Setter";
    String pojoName ;
    String insert = StringUtils.EMPTY;
    public String getCodingType() {
        return codingType;
    }

    public void setCodingType(String codingType) {
        this.codingType = codingType;
    }

    public String getPojoName() {
        return pojoName;
    }

    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }

    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }
}
