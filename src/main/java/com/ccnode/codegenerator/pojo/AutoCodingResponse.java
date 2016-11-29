package com.ccnode.codegenerator.pojo;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/11/04 20:15
 */
public class AutoCodingResponse extends BaseResponse{

    Map<String,String> userConfigMap = Maps.newHashMap();

    String projectPath;

    public Map<String, String> getUserConfigMap() {
        return userConfigMap;
    }

    public void setUserConfigMap(Map<String, String> userConfigMap) {
        this.userConfigMap = userConfigMap;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
}
