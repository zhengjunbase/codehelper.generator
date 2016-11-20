package com.ccnode.codegenerator.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p/>
 * Created by zhengjun.du on 2016/04/16 23:28
 */
public class GenCodeConfig {

    String projectPath = StringUtils.EMPTY;
    String serviceDir = StringUtils.EMPTY;
    String mapperDir = StringUtils.EMPTY;
    String sqlDir = StringUtils.EMPTY;
    String daoDir = StringUtils.EMPTY;
    String pojoName;
    Map<String,String> configMap = Maps.newHashMap();

    public Map<String, String> getConfigMap() {
        return configMap;
    }

    public void setConfigMap(Map<String, String> configMap) {
        this.configMap = configMap;
    }

    public String getPojoName() {
        return pojoName;
    }

    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getServiceDir() {
        return serviceDir;
    }

    public void setServiceDir(String serviceDir) {
        this.serviceDir = serviceDir;
    }

    public String getMapperDir() {
        return mapperDir;
    }

    public void setMapperDir(String mapperDir) {
        this.mapperDir = mapperDir;
    }

    public String getSqlDir() {
        return sqlDir;
    }

    public void setSqlDir(String sqlDir) {
        this.sqlDir = sqlDir;
    }

    public String getDaoDir() {
        return daoDir;
    }

    public void setDaoDir(String daoDir) {
        this.daoDir = daoDir;
    }
}
