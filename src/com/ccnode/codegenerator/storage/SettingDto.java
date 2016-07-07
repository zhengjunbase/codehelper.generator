package com.ccnode.codegenerator.storage;

import java.util.Date;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 14:28
 */
public class SettingDto {

    String userType;

    String lastRunTime;

    List<String> returnKey;

    List<Date> lastRegisterDate;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<String> getReturnKey() {
        return returnKey;
    }

    public void setReturnKey(List<String> returnKey) {
        this.returnKey = returnKey;
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
