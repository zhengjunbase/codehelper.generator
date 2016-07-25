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

    Map<String,Date> returnKeyMap = Maps.newHashMap();

    List<Date> lastRegisterDate = Lists.newArrayList();

    List<String> keyList = Lists.newArrayList();

    List<String> tkeyList = Lists.newArrayList();

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

    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }

    public List<String> getTkeyList() {
        return tkeyList;
    }

    public void setTkeyList(List<String> tkeyList) {
        this.tkeyList = tkeyList;
    }
}
