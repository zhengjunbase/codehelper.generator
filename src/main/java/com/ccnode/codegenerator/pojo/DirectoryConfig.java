package com.ccnode.codegenerator.pojo;

import com.ccnode.codegenerator.enums.FileType;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/22 17:09
 */
public class DirectoryConfig {
    
    Map<FileType,String> directoryMap = Maps.newHashMap();

    public Map<FileType, String> getDirectoryMap() {
        return directoryMap;
    }

    public void setDirectoryMap(Map<FileType, String> directoryMap) {
        this.directoryMap = directoryMap;
    }

}
