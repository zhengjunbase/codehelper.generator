package com.ccnode.codegenerator.storage;

import com.ccnode.codegenerator.enums.UserType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 14:28
 */
public class SettingDto {

    String userType = UserType.FREE_USER.name();

    Date lastRunTime = new Date();

    String uuid;

    Map<String,Date> returnKeyMap = Maps.newHashMap();

    List<Date> lastRegisterDate = Lists.newArrayList();

    List<String> eKeyList = Lists.newArrayList();

    String lKey;

    String eKey;

    String rKey;

    String registerTipMsg = "Please enter license Key : ";

    Date donatedDate;

    Date installedDate;

    Date updateDate;

    String version;

    String oldVersion;


    List<String> lKeyList = Lists.newArrayList();


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Map<String, Date> getReturnKeyMap() {
        return returnKeyMap;
    }

    public void setReturnKeyMap(Map<String, Date> returnKeyMap) {
        this.returnKeyMap = returnKeyMap;
    }

    public List<Date> getLastRegisterDate() {
        return lastRegisterDate;
    }

    public void setLastRegisterDate(List<Date> lastRegisterDate) {
        this.lastRegisterDate = lastRegisterDate;
    }

    public Date getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(Date lastRunTime) {
        this.lastRunTime = lastRunTime;
    }

    public List<String> geteKeyList() {
        return eKeyList;
    }

    public void seteKeyList(List<String> eKeyList) {
        this.eKeyList = eKeyList;
    }

    public List<String> getlKeyList() {
        return lKeyList;
    }

    public void setlKeyList(List<String> lKeyList) {
        this.lKeyList = lKeyList;
    }

    public String geteKey() {
        return eKey;
    }

    public void seteKey(String eKey) {
        this.eKey = eKey;
    }

    public String getlKey() {
        return lKey;
    }

    public void setlKey(String lKey) {
        this.lKey = lKey;
    }

    public String getRegisterTipMsg() {
        return registerTipMsg;
    }

    public void setRegisterTipMsg(String registerTipMsg) {
        this.registerTipMsg = registerTipMsg;
    }

    public String getrKey() {
        return rKey;
    }

    public void setrKey(String rKey) {
        this.rKey = rKey;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getDonatedDate() {
        return donatedDate;
    }

    public void setDonatedDate(Date donatedDate) {
        this.donatedDate = donatedDate;
    }

    public Date getInstalledDate() {
        return installedDate;
    }

    public void setInstalledDate(Date installedDate) {
        this.installedDate = installedDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getVersion() {
        return version;
    }

    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}
