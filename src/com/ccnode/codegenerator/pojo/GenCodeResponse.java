package com.ccnode.codegenerator.pojo;

import com.ccnode.codegenerator.util.GenCodeConfig;
import com.google.common.collect.Maps;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 19:54
 */
public class GenCodeResponse extends BaseResponse {

    Map<String,String> userConfigMap = Maps.newHashMap();

    GenCodeConfig codeConfig;

    DirectoryConfig directoryConfig;

    List<OnePojoInfo> pojoInfos;

    GenCodeRequest request;

    String pathSplitter;

    List<ChangeInfo> newFiles;

    List<ChangeInfo> updateFiles;

    public DirectoryConfig getDirectoryConfig() {
        return directoryConfig;
    }

    public void setDirectoryConfig(DirectoryConfig directoryConfig) {
        this.directoryConfig = directoryConfig;
    }

    public Map<String, String> getUserConfigMap() {
        return userConfigMap;
    }

    public void setUserConfigMap(Map<String, String> userConfigMap) {
        this.userConfigMap = userConfigMap;
    }

    public List<OnePojoInfo> getPojoInfos() {
        return pojoInfos;
    }

    public void setPojoInfos(List<OnePojoInfo> pojoInfos) {
        this.pojoInfos = pojoInfos;
    }

    public GenCodeRequest getRequest() {
        return request;
    }

    public void setRequest(GenCodeRequest request) {
        this.request = request;
    }

    public GenCodeConfig getCodeConfig() {
        return codeConfig;
    }

    public void setCodeConfig(GenCodeConfig codeConfig) {
        this.codeConfig = codeConfig;
    }

    public String getPathSplitter() {
        return pathSplitter;
    }

    public void setPathSplitter(String pathSplitter) {
        this.pathSplitter = pathSplitter;
    }

    public List<ChangeInfo> getNewFiles() {
        return newFiles;
    }

    public void setNewFiles(List<ChangeInfo> newFiles) {
        this.newFiles = newFiles;
    }

    public List<ChangeInfo> getUpdateFiles() {
        return updateFiles;
    }

    public void setUpdateFiles(List<ChangeInfo> updateFiles) {
        this.updateFiles = updateFiles;
    }
}
