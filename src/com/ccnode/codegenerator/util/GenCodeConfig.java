package com.ccnode.codegenerator.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p/>
 * Created by zhengjun.du on 2016/04/16 23:28
 */
public class GenCodeConfig {

    String projectPath;
    String servicePath;
    String mapperPath;
    String sqlPath;
    String daoPath;
    String pojoName;
    Map<String,String> configMap = Maps.newHashMap();

    public void refreshPathByPojoName(){
        servicePath = servicePath +"/" + pojoName + "Service.java";
        mapperPath = mapperPath +"/" + pojoName + "Dao.xml";
        daoPath = daoPath + "/" +pojoName + "Dao.java";
        sqlPath = sqlPath + "/" +pojoName + "alter.sql";
        servicePath = correctPath(servicePath);
        mapperPath = correctPath(mapperPath);
        daoPath = correctPath(daoPath);
        sqlPath = correctPath(sqlPath);
    }

    public String correctPath(String oldPath){
        if(oldPath.contains("//")) {
            return oldPath.replace("//","/");
        }
        return oldPath;
    }

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

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public String getMapperPath() {
        return mapperPath;
    }

    public void setMapperPath(String mapperPath) {
        this.mapperPath = mapperPath;
    }

    public String getSqlPath() {
        return sqlPath;
    }

    public void setSqlPath(String sqlPath) {
        this.sqlPath = sqlPath;
    }

    public String getDaoPath() {
        return daoPath;
    }

    public void setDaoPath(String daoPath) {
        this.daoPath = daoPath;
    }
}
