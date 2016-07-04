package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojo.RetStatus;
import com.ccnode.codegenerator.pojoHelper.OnePojoInfoHelper;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.IOUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            checkResponse(response);

            response.setRequest(request);
            checkResponse(response);

            response.setPathSplitter(request.getPathSplitter());
            checkResponse(response);

            response = UserConfigService.initConfig(response);
            checkResponse(response);

            response = initPojos(response);

            checkResponse(response);
            GenSqlService.genSQL(response);

            GenDaoService.genDAO(response);
            checkResponse(response);

            GenServiceService.genService(response);
            checkResponse(response);

            GenMapperService.genMapper(response);
            checkResponse(response);
            for (OnePojoInfo onePojoInfo : response.getPojoInfos()) {
                OnePojoInfoHelper.flushFiles(onePojoInfo,response);
            }
            response.success();

        }catch(Exception e){
            e.printStackTrace();
            response.failure(RetStatus.BOOKING_ERROR,e.getMessage());
        }
        return response;
    }

    private static void checkResponse(GenCodeResponse response) {
        if(response.checkFailure()){
            System.out.println(response.getMsg());
            System.out.println(response.getCode());
            throw new RuntimeException("check failure");
        }
    }

    private static GenCodeResponse initPojos(GenCodeResponse response) {
        List<OnePojoInfo> contextList = Lists.newArrayList();
        response.setPojoInfos(contextList);
        for (String pojoName : response.getRequest().getPojoNames()) {
            OnePojoInfo onePojoInfo = new OnePojoInfo();
            try{
                onePojoInfo.setPojoName(pojoName);
                onePojoInfo.setPojoClassSimpleName(pojoName);
                String fullPojoPath = IOUtils.matchOnlyOneFile(response.getRequest().getProjectPath(), pojoName +  ".java");
                onePojoInfo.setFullPojoPath(fullPojoPath);
                onePojoInfo.setPojoPackage(GenCodeUtil.getPojoPackage(fullPojoPath));
//              Runtime.getRuntime().exec("javac "+fullPojoPath);
//              String projectPath = response.getRequest().getProjectPath();
//              Class pojoClass = GenCodeUtil.loadClassByFileName(projectPath, pojoName);
//              onePojoInfo.setPojoClass(pojoClass);
                String daoPath = response.getCodeConfig().getDaoPath();
                onePojoInfo.setDaoPackage(GenCodeUtil.pathToPackage(daoPath));
                onePojoInfo.setServicePackage(GenCodeUtil.pathToPackage(response.getCodeConfig().getServicePath()));

                OnePojoInfoHelper.parseIdeaFieldInfo(onePojoInfo, response);
//              OnePojoInfoHelper.parsePojoFieldInfo(onePojoInfo);
                OnePojoInfoHelper.parseFiles(onePojoInfo,response);
                contextList.add(onePojoInfo);
            }catch(Exception e){
                return response.failure("","解析Class:"+pojoName + "失败");
            }
        }
        return response;
    }

    public static void main(String[] args) throws IOException {
        LOGGER.info("main");

        Process ls = Runtime.getRuntime().exec("ls -lrthu");
        InputStream stream = ls.getInputStream();
        List<String> strings = org.apache.commons.io.IOUtils.readLines(stream);
        LOGGER.info("s:{}",strings);
    }
}
