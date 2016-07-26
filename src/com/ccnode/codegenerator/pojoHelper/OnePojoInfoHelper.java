package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.pojo.*;
import com.ccnode.codegenerator.util.IOUtils;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.RegexUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.impl.source.javadoc.PsiDocCommentImpl;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import static org.apache.commons.io.IOUtils.writeLines;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 21:50
 */
public class OnePojoInfoHelper {

    private final static Logger LOGGER = LoggerWrapper.getLogger(OnePojoInfoHelper.class);

    @NotNull
    public static Boolean containSplitKey(@NotNull OnePojoInfo onePojoInfo, String splitKey){
        for (PojoFieldInfo pojoFieldInfo : onePojoInfo.getPojoFieldInfos()) {
            if(pojoFieldInfo.getFieldName().equalsIgnoreCase(splitKey)){
                return true;
            }
        }
        return false;
    }

    public static void parseIdeaFieldInfo(@NotNull OnePojoInfo onePojoInfo, GenCodeResponse response){
        String pojoName = onePojoInfo.getPojoName();
        String pojoFileShortName = pojoName + ".java";
        Project project = response.getRequest().getProject();
        PsiFile[] psiFile = FilenameIndex
                .getFilesByName(project, pojoFileShortName, new EverythingGlobalScope(project));
        if(psiFile.length != 1){
            // todo
        }
        PsiElement firstChild = psiFile[0].getFirstChild();
        List<PsiElement> elements = Lists.newArrayList();
        if(firstChild instanceof PsiClassImpl){
            elements.add(firstChild);
        }
        while (firstChild.getNextSibling() != null){
            firstChild = firstChild.getNextSibling();
            if(firstChild instanceof PsiClassImpl){
                elements.add(firstChild);
            }
        }
        if(elements.size() != 1){
            // todo
        }

        PsiClassImpl psiClass = (PsiClassImpl) elements.get(0);
        PsiElement context = psiClass.getContext();
        if(context == null){
            throw new RuntimeException("parse class error");
        }
        String text = context.getText();
        onePojoInfo.setPojoPackage(parsePojoPackage(text));
        PsiField[] allFields = psiClass.getAllFields();
        List<PojoFieldInfo> fieldList = Lists.newArrayList();

        for (PsiField field : allFields) {
            if(isStaticField(field)){
                continue;
            }
            parseComment(field);
            PojoFieldInfo fieldInfo = new PojoFieldInfo();
            fieldInfo.setFieldComment(parseComment(field));
            PsiType type = field.getType();
            fieldInfo.setFieldName(field.getName());
            fieldInfo.setFieldClass(type.getPresentableText());
            fieldInfo.setAnnotations(Lists.newArrayList());
            fieldList.add(fieldInfo);
        }
        onePojoInfo.setPojoFieldInfos(fieldList);
    }

    private static Boolean isStaticField(@NotNull PsiField field){
        PsiElement[] children = field.getChildren();
        for (PsiElement child : children) {
            String text = child.getText();
            if(text.contains(" static ")){
                return true;
            }
        }
        return false;
    }

    private static String parsePojoPackage(String context){
        List<String> lines = Splitter.on("\n").trimResults().omitEmptyStrings().splitToList(context);
        for (String line : lines) {
            String match = RegexUtil.getMatch("[\\s]*package[\\s]+.+[\\s]*;", line);
            if(StringUtils.isNotBlank(match)){
                String pojoPackage = Splitter.on("package").trimResults().omitEmptyStrings().splitToList(match).get(0);
                pojoPackage = pojoPackage.replace(";","");
                pojoPackage = pojoPackage.replace(" ","");
                return pojoPackage;
            }
        }
        return StringUtils.EMPTY;
    }
    private static String parseComment(PsiField field) {
        if(field == null){
            return StringUtils.EMPTY;
        }
        PsiElement[] children = field.getChildren();
        for (PsiElement child : children) {
            String text1 = child.getText();
            if(child instanceof PsiDocCommentImpl){
                String text = child.getText();
                text = text.replace("/*","");
                text = text.replace("*/","");
                text = text.replace("//","");
                text = text.replace("\n","");
                text = text.replace("*","");
                text = text.trim();
                return text;
            }
            if(child instanceof  PsiCommentImpl){
                String text = child.getText();
                text = text.replace("/*","");
                text = text.replace("*/","");
                text = text.replace("//","");
                text = text.replace("\n","");
                text = text.replace("*","");
                text = text.trim();
                return text;
            }
        }
        return StringUtils.EMPTY;
    }

    public static void parsePojoFieldInfo(@NotNull OnePojoInfo onePojoInfo){
        @NotNull Class pojoClass = onePojoInfo.getPojoClass();
        Field[] fields = pojoClass.getDeclaredFields();
        if(fields == null || fields.length == 0){
            return;
        }
        List<PojoFieldInfo> fieldInfoList = Lists.newArrayList();
        onePojoInfo.setPojoFieldInfos(fieldInfoList);
        for (Field field : fields) {
            PojoFieldInfo fieldInfo = new PojoFieldInfo();
            fieldInfo.setFieldClass(StringUtils.EMPTY);
            fieldInfo.setFieldName(field.getName());
            fieldInfo.setAnnotations(Lists.newArrayList(field.getDeclaredAnnotations()));
            fieldInfoList.add(fieldInfo);
        }
    }

    public static void parseFiles(OnePojoInfo onePojoInfo, GenCodeResponse response) {
        onePojoInfo.setFiles(Lists.newArrayList());
        for (FileType fileType : FileType.values()) {
            if(fileType == FileType.NONE){
                continue;
            }
            GeneratedFile file = new GeneratedFile();
            file.setFileType(fileType);
            String filePath = StringUtils.EMPTY;
            switch (fileType){
             case SQL:
                 filePath = onePojoInfo.getFullSqlPath();
                break;
            case MAPPER:
                 filePath = onePojoInfo.getFullMapperPath();
                break;
            case SERVICE:
                 filePath = onePojoInfo.getFullServicePath();
                break;
            case DAO:
                 filePath = onePojoInfo.getFullDaoPath();
                break;
            }
            file.setFilePath(filePath);
            file.setFile(new File(file.getFilePath()));
            if(!file.getFile().exists()){
             file.getFile().getParentFile().mkdirs();
                try {
                    file.getFile().createNewFile();
                } catch (Exception e) {
                    response.failure("create file : " +file.getFile().getAbsolutePath() +" failure",e);
                }
            }
            try{
                if(file.getFile().exists()){
                    file.setOldLines(Lists.newArrayList(IOUtils.readLines(file.getFile())));
                }else{
                    file.setOldLines(Lists.newArrayList());
                }

            }catch(Exception e){
                response.failure("read" + file.getFile().getAbsolutePath() +" failure",e);
            }
            file.setNewLines(Lists.newArrayList());
            onePojoInfo.getFiles().add(file);
        }
    }

    // todo
    public static BaseResponse flushFiles(@NotNull OnePojoInfo onePojoInfo, GenCodeResponse response){

        try{
            for (GeneratedFile generatedFile : onePojoInfo.getFiles()) {
                writeLines(generatedFile.getNewLines(), "\n", new FileOutputStream(generatedFile.getFile()));
            }
            return response;
        }catch(Exception e){
            LOGGER.info("flush file error",e);
            return response.failure("flush file error",e);
        }

    }

    public static void main(String[] args) {
        String match = RegexUtil.getMatch("[\\s]*package[\\s]+.+[\\s]*;", "   package cox3m.qunar.Sn_+surance.service.dto  ;  \n");
            if(StringUtils.isNotBlank(match)){
                String pack = Splitter.on("package").trimResults().omitEmptyStrings().splitToList(match).get(0);
                pack = pack.replace(";","");
                pack = pack.replace(" ","");
            }
    }
}
