package com.ccnode.codegenerator.genCode;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojo.GeneratedFile;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;

import static com.ccnode.codegenerator.util.GenCodeUtil.ONE_RETRACT;
import static com.ccnode.codegenerator.util.GenCodeUtil.TWO_RETRACT;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/28 21:14
 */
public class GenServiceService {

    public static void genService( GenCodeResponse response) {
        for (OnePojoInfo pojoInfo : response.getPojoInfos()) {
            GeneratedFile fileInfo = GenCodeResponseHelper.getByFileType(pojoInfo, FileType.SERVICE);
            Boolean useGenericDao = Objects.equal(response.getUserConfigMap().get("usegenericdao"),"true");
            genDaoFile(pojoInfo,fileInfo,useGenericDao);
        }
    }

    private static void genDaoFile(OnePojoInfo onePojoInfo, GeneratedFile fileInfo, Boolean useGenericDao) {
        String pojoName = onePojoInfo.getPojoName();
        String pojoNameDao = pojoName + "Dao";
        if (!fileInfo.getOldLines().isEmpty()) {
            fileInfo.setNewLines(fileInfo.getOldLines());
            return;
        }
        if(useGenericDao){
            List<String> newLines = Lists.newArrayList();
            newLines.add("package "+ onePojoInfo.getServicePackage() + ";");
            newLines.add("");
            newLines.add("import org.springframework.stereotype.Service;");
            newLines.add("import javax.annotation.Resource;");
            newLines.add("import java.util.List;");
            newLines.add("import "+ onePojoInfo.getPojoPackage() + "." +onePojoInfo.getPojoName() + ";");
            newLines.add("import "+ onePojoInfo.getDaoPackage() + "." +onePojoInfo.getPojoName() + "Dao;");
            newLines.add("");
            newLines.add("@Service");
            newLines.add("public class " + pojoName + "Service extends GenericService<" + pojoName + "> {");
            newLines.add("");
            newLines.add("    @Resource");
            newLines.add(ONE_RETRACT + "private " + pojoName + "Dao " + GenCodeUtil.getLowerCamel(pojoName) + "Dao;");
            newLines.add("");
            newLines.add("    @Override");
            newLines.add(ONE_RETRACT + "public GenericDao<" + pojoName + "> getGenericDao() {");
            newLines.add(TWO_RETRACT + "return " + GenCodeUtil.getLowerCamel(pojoNameDao) + ";");
            newLines.add(ONE_RETRACT + "}");
            newLines.add("}");
            fileInfo.setNewLines(newLines);
        }else{
            List<String> newLines = Lists.newArrayList();
            String daoName = GenCodeUtil.getLowerCamel(pojoName) + "Dao";
            newLines.add("package "+ onePojoInfo.getServicePackage() + ";");
            newLines.add("");
            newLines.add("import org.springframework.stereotype.Service;");
            newLines.add("import java.util.List;");
            newLines.add("import "+ onePojoInfo.getPojoPackage() + "." +onePojoInfo.getPojoName() + ";");
            newLines.add("import "+ onePojoInfo.getDaoPackage() + "." +onePojoInfo.getPojoName() + "Dao;");
            newLines.add("");
            newLines.add("@Service");
            newLines.add("public class " + pojoName + "Service {");
            newLines.add("");
            newLines.add("    @Resource");
            newLines.add(ONE_RETRACT + "private " + pojoName + "Dao " + daoName + ";");
            newLines.add("");
            newLines.add(ONE_RETRACT + "public int add("+pojoName +" pojo){");
            newLines.add(TWO_RETRACT + "return "+daoName + ".add(pojo);");
            newLines.add(ONE_RETRACT + "}");
            newLines.add("");
            newLines.add(ONE_RETRACT +"public int adds(List< "+pojoName +"> pojos){");
            newLines.add(TWO_RETRACT + "return "+daoName + ".adds(pojos);");
            newLines.add(ONE_RETRACT + "}");
            newLines.add("");
            newLines.add(ONE_RETRACT + "public List<"+pojoName+"> query("+pojoName +" pojo){");
            newLines.add(TWO_RETRACT + "return "+daoName + ".query(pojo);");
            newLines.add(ONE_RETRACT + "}");
            newLines.add("");
            newLines.add(ONE_RETRACT +"public int update("+pojoName +" pojo){");
            newLines.add(TWO_RETRACT + "return "+daoName + ".update(pojo);");
            newLines.add(ONE_RETRACT + "}");
            newLines.add("");
            newLines.add("}");
            fileInfo.setNewLines(newLines);
        }

    }
}
