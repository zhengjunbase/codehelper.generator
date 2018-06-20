package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.genCode.UserConfigService;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojo.GeneratedFile;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/22 15:15
 */
public class GenCodeResponseHelper {

    private static GenCodeResponse response;

    public static void setResponse(GenCodeResponse response) {
        GenCodeResponseHelper.response = response;
    }

    public static GenCodeResponse getResponse() {
        return response;
    }

    public static GeneratedFile getByFileType(@NotNull OnePojoInfo onePojoInfo, FileType type){
        String mapperSuffix = UserConfigService.removeStartAndEndSplitter(response.getUserConfigMap().get("mapper.suffix"));
        String daoSuffix = UserConfigService.removeStartAndEndSplitter(response.getUserConfigMap().get("dao.suffix"));
        String serviceSuffix = UserConfigService.removeStartAndEndSplitter(response.getUserConfigMap().get("service.suffix"));
        String suffix = StringUtils.EMPTY;
        switch (type){
            case MAPPER:
                suffix = mapperSuffix;
                break;
            case DAO:
                suffix = daoSuffix;
                break;
            case SERVICE:
                suffix = serviceSuffix;
                break;
        }
        for (GeneratedFile generatedFile : onePojoInfo.getFiles()) {
            if(type == FileType.NONE){
                continue;
            }
            if((suffix+generatedFile.getFileType().getSuffix()).equals(suffix+ type.getSuffix())){
                return generatedFile;
            }
        }
        // todo
        throw new RuntimeException("获取文件错误");
    }
    public static Boolean isUseGenericDao(GenCodeResponse response){
        return getSwitch(response,"usegenericdao");
    }

    public static Boolean getSwitch(GenCodeResponse response, String key){
        return response != null && response.getUserConfigMap() != null && Objects.equal( response.getUserConfigMap().get(key),"true");
    }

    public static String getProjectPathWithSplitter(GenCodeResponse response){
        String projectPath = response.getRequest().getProjectPath();
        if(StringUtils.isBlank(projectPath)){
            throw new RuntimeException("error, projectPath is empty");
        }
                if(!projectPath.endsWith(response.getPathSplitter())){
                    projectPath += response.getPathSplitter();
                }
        return projectPath;
    }

    public static String getPathSplitter(){
        return System.getProperty("file.separator");
    }
    @Nullable
    public static String getSplitKey(GenCodeResponse response){
        return response.getUserConfigMap().get("splitkey");
    }

}
