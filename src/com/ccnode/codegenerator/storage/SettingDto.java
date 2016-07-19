package com.ccnode.codegenerator.storage;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 14:28
 */
public class SettingDto {

    String userType;

    String lastRunTime;

    Map<String,Date> returnKeyMap;

    List<Date> lastRegisterDate;

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

    public String getLastRunTime() {
        return lastRunTime;
    }

    public void setLastRunTime(String lastRunTime) {
        this.lastRunTime = lastRunTime;
    }
}
