package com.ccnode.codegenerator.test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 11:49
 */
public class TestPojo {

    String hello;
    Integer integer;
    Double doubler;
    BigDecimal bigDecimal;
    Float aFloat;
    Short aShort;
    Date date;
    private Long id;
    private Long sid;
    private Date createTime;
    private Long xsid;
    private Long xid;
    private Date lastUpdate;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public Double getDoubler() {
        return doubler;
    }

    public void setDoubler(Double doubler) {
        this.doubler = doubler;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }

    public void setBigDecimal(BigDecimal bigDecimal) {
        this.bigDecimal = bigDecimal;
    }

    public Float getaFloat() {
        return aFloat;
    }

    public void setaFloat(Float aFloat) {
        this.aFloat = aFloat;
    }

    public Short getaShort() {
        return aShort;
    }

    public void setaShort(Short aShort) {
        this.aShort = aShort;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
