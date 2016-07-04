package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.pojo.DirectoryConfig;
import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.util.GenCodeConfig;
import com.ccnode.codegenerator.util.IOUtils;
import com.ccnode.codegenerator.util.PojoUtil;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 12:40
 */
public class UserConfigService {


    public static GenCodeResponse initConfig(GenCodeResponse response){

        try{
            GenCodeConfig config = new GenCodeConfig();
            Map<String, String> userConfigMap = response.getUserConfigMap();
            String pojos = userConfigMap.get("pojos");
            pojos = pojos.replace(",","|");
            response.getRequest().setPojoNames(Splitter.on("|").trimResults().omitEmptyStrings().splitToList(pojos));
            for (Map.Entry<String, String> configEntry : response.getUserConfigMap().entrySet()) {
                String key = configEntry.getKey();
                String value = configEntry.getValue();
                check(response.getRequest().getProjectPath(), key,value,config);
                response.setCodeConfig(config);
                DirectoryConfig directoryConfig = new DirectoryConfig();
                response.setDirectoryConfig(directoryConfig);
                config.setDaoPath(avoidNullPath(userConfigMap.get("dao.path"),response));
                config.setSqlPath(avoidNullPath(userConfigMap.get("sql.path"),response));
                config.setMapperPath(avoidNullPath(userConfigMap.get("mapper.path"),response));
                config.setServicePath(avoidNullPath(userConfigMap.get("service.path"),response));
                String bathPath = response.getRequest().getProjectPath();
                directoryConfig.getDirectoryMap().put(FileType.MAPPER, bathPath + response.getPathSplitter() + config.getMapperPath());
                directoryConfig.getDirectoryMap().put(FileType.DAO, bathPath + response.getPathSplitter() + config.getDaoPath());
                directoryConfig.getDirectoryMap().put(FileType.SQL, bathPath + response.getPathSplitter() + config.getSqlPath());
                directoryConfig.getDirectoryMap().put(FileType.SERVICE, bathPath + response.getPathSplitter() + config.getServicePath());
            }
        }catch(Exception e){
            return response.failure(""," status error occurred");
        }

        return response;
    }

    public static String avoidNullPath(String path, GenCodeResponse response){
        String bathPath = response.getRequest().getProjectPath();
        if(StringUtils.isBlank(path)){
            return bathPath;
        }
        //todo
        return path;
    }


    // todo
    private static void check(String projectPath, String key, String value, GenCodeConfig config) {
//        List<String> fileName = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(value);
//        String fileName = IOUtils.matchOnlyOneFile(projectPath, "value");

    }

    public static GenCodeResponse readConfigFile(GenCodeRequest request){
        GenCodeResponse ret = new GenCodeResponse();
        ret.accept();
        try{
            String projectPath = request.getProjectPath();
            String fileName = IOUtils.matchOnlyOneFile(projectPath, "generator.properties");
            if(StringUtils.isBlank(fileName)){
                return ret.failure("","error, no generator.properties config file,"
                        + "please add an generator.properties in you poject path");
            }
            if(Objects.equal(fileName,"NOT_ONLY")){
                return ret.failure("","error, duplicated generator.properties file");
            }
            File configFile = new File(fileName);
            List<String> strings = IOUtils.readLines(configFile);
            strings = PojoUtil.avoidEmptyList(strings);
            int lineIndex = 1;
            Map<String,String> configMap = Maps.newHashMap();
            for (String configLine : strings) {
                lineIndex ++;
                if(StringUtils.isBlank(configLine) || configLine.startsWith("#")){
                    continue;
                }
                if(configLine.startsWith("=")){
                    return ret.failure("","line: "+ lineIndex + "error, config key con not be empty");
                }
                List<String> split = Splitter.on("=").trimResults().omitEmptyStrings().splitToList(configLine);
                if(split.size() != 2){
                    return ret.failure("","line: "+ lineIndex + "error, correct format : key = value");
                }
                configMap.put(split.get(0).toLowerCase(),split.get(1));
            }
            ret.setUserConfigMap(configMap);
            return ret;
        }catch(IOException e){
            return ret.failure(""," readConfigFile config file read error ");
        }catch(Exception e){
            return ret.failure("","readConfigFile error occurred");
        }
    }
}
