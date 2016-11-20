
package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.pojo.*;
import com.ccnode.codegenerator.util.*;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
        onePojoInfo.setPojoPackage(parsePackage(text));
        PsiField[] allFields = psiClass.getAllFields();
        List<PojoFieldInfo> fieldList = Lists.newArrayList();

        for (PsiField field : allFields) {
            if(isStaticField(field)){
                continue;
            }
            if(!isSupportType(field.getType().getPresentableText())){
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

    private static Boolean isSupportType(String fieldType){
        if(StringUtils.isBlank(fieldType)){
            return false;
        }
        List<String> supportList= ImmutableList.of("string","integer","int","short","date","long","bigdecimal","double","float");
        return supportList.contains(fieldType.toLowerCase());
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

    private static String parsePackage(String context){
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

    public static void deduceDaoPackage(OnePojoInfo onePojoInfo, GenCodeResponse response){
        GeneratedFile daoFile = getFileByType(onePojoInfo, FileType.DAO);
        if(daoFile == null){
            return;
        }
        String deducePackage = GenCodeUtil.deducePackage(StringUtils.defaultIfEmpty(response.getCodeConfig().getDaoDir(),onePojoInfo.getPojoDirPath()) ,onePojoInfo.getPojoPackage());
        for (String s : daoFile.getOriginLines()) {
            if(s.trim().contains("package ")){
                deducePackage = parsePackage(s);
                break;
            }
        }
        onePojoInfo.setDaoPackage(deducePackage);
    }

    public static void deduceServicePackage(OnePojoInfo onePojoInfo, GenCodeResponse response){
        GeneratedFile serviceFile = getFileByType(onePojoInfo, FileType.SERVICE);
        if(serviceFile == null){
            return;
        }
        String deducePackage = GenCodeUtil.deducePackage(StringUtils.defaultIfEmpty(response.getCodeConfig().getServiceDir(),onePojoInfo.getPojoDirPath()) ,onePojoInfo.getPojoPackage());
        for (String s : serviceFile.getOriginLines()) {
            if(s.trim().contains("package ")){
                deducePackage = parsePackage(s);
                break;
            }
        }
        onePojoInfo.setServicePackage(deducePackage);
    }

    @Nullable
    public static GeneratedFile getFileByType(OnePojoInfo onePojoInfo, FileType fileType){
        for (GeneratedFile generatedFile : onePojoInfo.getFiles()) {
            if(generatedFile.getFileType() == fileType){
                return generatedFile;
            }
        }
        return null;
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
                    file.setOriginLines(Lists.newArrayList(IOUtils.readLines(file.getFile())));
                }else{
                    file.setOldLines(Lists.newArrayList());
                    file.setOriginLines(Lists.newArrayList());
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
                List<String> lines = generatedFile.getNewLines();
                if(lines == null || lines.isEmpty()){
                    lines = generatedFile.getOriginLines();
                }
                writeLines(lines, "\n", new FileOutputStream(generatedFile.getFile()));
            }
            Pair<List<ChangeInfo>, List<ChangeInfo>> pair = statisticChange(response.getPojoInfos());
            LOGGER.info(" flushFiles :{},pair.getRight():{}",pair.getLeft(),pair.getRight());
            response.setNewFiles(pair.getLeft());
            response.setUpdateFiles(pair.getRight());
            return response;
        }catch(Exception e){
            LOGGER.info("flush file error",e);
            return response.failure("flush file error",e);
        }

    }

    public static Pair<List<ChangeInfo>, List<ChangeInfo>> statisticChange(List<OnePojoInfo> onePojoInfos){
        List<ChangeInfo> newFiles = Lists.newArrayList();
        List<ChangeInfo> updatedFiles = Lists.newArrayList();
        for (OnePojoInfo onePojoInfo : onePojoInfos) {
            for (GeneratedFile file : onePojoInfo.getFiles()) {
                if(file.getOriginLines().isEmpty()){
                    ChangeInfo newFile = new ChangeInfo();
                    newFile.setFileName(file.getFile().getName());
                    newFile.setAffectRow(file.getNewLines().size());
                    newFile.setChangeType("new file");
                    newFiles.add(newFile);
                }else{
                    ChangeInfo updatedFile = new ChangeInfo();
                    updatedFile.setFileName(file.getFile().getName());
                    updatedFile.setAffectRow(countChangeRows(file.getNewLines(), file.getOriginLines()));
                    updatedFile.setChangeType("updated");
                    updatedFiles.add(updatedFile);
                }
            }
        }
        return Pair.of(newFiles, updatedFiles);
        
    }

    private static Integer countChangeRows(List<String> oldLines, List<String> newLines) {
        oldLines = PojoUtil.avoidEmptyList(oldLines);
        newLines = PojoUtil.avoidEmptyList(newLines);
        Integer changeCount = 0;
        for (String oldLine : oldLines) {
            if(!newLines.contains(oldLine)){
                changeCount ++;
            }
        }

        for (String line : newLines) {
            if(!oldLines.contains(line)){
                changeCount ++;
            }
        }
        return changeCount;
    }

    public static void main(String[] args) {

        List<Integer> xList = Lists.newArrayList(0,1,2,3);
        List<Integer> yList = Lists.newArrayList(2,3,4,5,6);
        System.out.println(Sets.intersection(Sets.newHashSet(xList), Sets.newHashSet(yList)));

        String match = RegexUtil.getMatch("[\\s]*package[\\s]+.+[\\s]*;", "   package cox3m.qunar.Sn_+surance.service.dto  ;  \n");
            if(StringUtils.isNotBlank(match)){
                String pack = Splitter.on("package").trimResults().omitEmptyStrings().splitToList(match).get(0);
                pack = pack.replace(";","");
                pack = pack.replace(" ","");
            }
    }

}
