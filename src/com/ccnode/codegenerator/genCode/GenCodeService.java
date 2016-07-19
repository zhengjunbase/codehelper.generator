package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojo.PojoFieldInfo;
import com.ccnode.codegenerator.pojoHelper.OnePojoInfoHelper;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.IOUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 19:52
 */
public class GenCodeService {

    private final static Logger LOGGER = LoggerFactory.getLogger(GenCodeService.class);

    public static GenCodeResponse genCode(GenCodeRequest request){
        GenCodeResponse response = new GenCodeResponse();
        try{
            response = UserConfigService.readConfigFile(request);
            if(response.checkFailure()){
                return response;
            }

            response.setRequest(request);
            if(response.checkFailure()){
                return response;
            }
            response.setPathSplitter(request.getPathSplitter());
            if(response.checkFailure()){
                return response;
            }

            response = UserConfigService.initConfig(response);
            if(response.checkFailure()){
                return response;
            }

            response = initPojos(response);
            if(response.checkFailure()){
                return response;
            }

            GenSqlService.genSQL(response);
            if(response.checkFailure()){
                return response;
            }

            GenDaoService.genDAO(response);
            if(response.checkFailure()){
                return response;
            }

            GenServiceService.genService(response);
            if(response.checkFailure()){
                return response;
            }

            GenMapperService.genMapper(response);
            if(response.checkFailure()){
                return response;
            }

            for (OnePojoInfo onePojoInfo : response.getPojoInfos()) {
                OnePojoInfoHelper.flushFiles(onePojoInfo,response);
            }
            response.success();

        }catch(Exception e){
            response.failure("gen code failure ",e);
        }
        return response;
    }


    private static GenCodeResponse initPojos(GenCodeResponse response) {
        List<OnePojoInfo> contextList = Lists.newArrayList();
        response.setPojoInfos(contextList);
        for (String pojoName : response.getRequest().getPojoNames()) {
            OnePojoInfo onePojoInfo = new OnePojoInfo();
            try{
                onePojoInfo.setPojoName(pojoName);
                onePojoInfo.setPojoClassSimpleName(pojoName);
                File pojoFile = IOUtils.matchOnlyOneFile(response.getRequest().getProjectPath(), pojoName + ".java");
                if(pojoFile == null){
                    return response.failure("", pojoName + " file not exist");
                }
                String fullPojoPath = pojoFile.getAbsolutePath();
                onePojoInfo.setFullPojoPath(fullPojoPath);
                onePojoInfo.setPojoPackage(GenCodeUtil.getPojoPackage(fullPojoPath));
                String daoPath = response.getCodeConfig().getDaoPath();
                onePojoInfo.setDaoPackage(GenCodeUtil.pathToPackage(daoPath));
                onePojoInfo.setServicePackage(GenCodeUtil.pathToPackage(response.getCodeConfig().getServicePath()));

                OnePojoInfoHelper.parseIdeaFieldInfo(onePojoInfo, response);
                List<PojoFieldInfo> pojoFieldInfos = onePojoInfo.getPojoFieldInfos();
                String concat = StringUtils.EMPTY;
                for (PojoFieldInfo pojoFieldInfo : pojoFieldInfos) {
                    concat += "|"+pojoFieldInfo.getFieldName();
                }
                if(!concat.contains("id")){
                    return response.failure(pojoName + " should has 'id' field");
                }
                OnePojoInfoHelper.parseFiles(onePojoInfo,response);
                contextList.add(onePojoInfo);
            }catch(Exception e){
                return response.failure("","parse Class:"+pojoName + "failure");
            }
        }
        return response;
    }
}
