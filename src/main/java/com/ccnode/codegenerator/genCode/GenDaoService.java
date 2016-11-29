package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.enums.MethodName;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojo.GeneratedFile;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;

import java.util.List;

import static com.ccnode.codegenerator.util.GenCodeUtil.ONE_RETRACT;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/28 21:14
 */
public class GenDaoService {

    private final static Logger LOGGER = LoggerWrapper.getLogger(GenDaoService.class);

    public static void genDAO(GenCodeResponse response) {
        for (OnePojoInfo pojoInfo : response.getPojoInfos()) {
            try{
                GeneratedFile fileInfo = GenCodeResponseHelper.getByFileType(pojoInfo, FileType.DAO);
                genDaoFile(pojoInfo,fileInfo,GenCodeResponseHelper.isUseGenericDao(response));
            }catch(Throwable e){
                LOGGER.error("GenDaoService genDAO error", e);
                response.failure("GenDaoService genDAO error");
            }
        }
    }

    private static void genDaoFile(OnePojoInfo onePojoInfo, GeneratedFile fileInfo, Boolean useGenericDao) {
        String pojoName = onePojoInfo.getPojoName();
        String pojoNameDao = pojoName+"Dao";
        if(!fileInfo.getOldLines().isEmpty()){
            fileInfo.setNewLines(fileInfo.getOldLines());
            return;
        }
        if(useGenericDao){
            LOGGER.info("genDaoFile useGenericDao true");
            List<String> newLines = Lists.newArrayList();
            newLines.add("package "+ onePojoInfo.getDaoPackage() + ";");
            newLines.add("");
            newLines.add("import java.util.List;");
            newLines.add("import org.apache.ibatis.annotations.Param;");
            newLines.add("import "+ onePojoInfo.getPojoPackage() + "." +onePojoInfo.getPojoName() + ";");
            newLines.add("");
            newLines.add("public interface "+pojoNameDao +" extends GenericDao<"+pojoName+"> {");
            newLines.add("");
            newLines.add("}");
            fileInfo.setNewLines(newLines);
        }else{
            LOGGER.info("genDaoFile useGenericDao false");
            List<String> newLines = Lists.newArrayList();
            newLines.add("package "+ onePojoInfo.getDaoPackage() + ";");
            newLines.add("");
            newLines.add("import org.apache.ibatis.annotations.Param;");
            newLines.add("import java.util.List;");
            newLines.add("import "+ onePojoInfo.getPojoPackage() + "." +onePojoInfo.getPojoName() + ";");
            newLines.add("");
            newLines.add("public interface "+pojoNameDao+" {");
            newLines.add("");
            newLines.add(ONE_RETRACT + "int "+ MethodName.insert.name() +"(@Param(\"pojo\") "+pojoName +" pojo);");
            newLines.add("");
            newLines.add(ONE_RETRACT + "int "+ MethodName.insertList.name() +"(@Param(\"pojos\") List< "+pojoName +"> pojo);");
            newLines.add("");
            newLines.add(ONE_RETRACT + "List<"+pojoName+"> "+ MethodName.select.name() +"(@Param(\"pojo\") "+pojoName +" pojo);");
            newLines.add("");
            newLines.add(ONE_RETRACT + "int "+ MethodName.update.name() +"(@Param(\"pojo\") "+pojoName +" pojo);");
            newLines.add("");
            newLines.add("}");
            fileInfo.setNewLines(newLines);
        }
    }

}
