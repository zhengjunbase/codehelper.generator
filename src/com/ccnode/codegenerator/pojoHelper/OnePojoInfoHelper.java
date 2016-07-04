package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.enums.FileType;
import com.ccnode.codegenerator.pojo.*;
import com.ccnode.codegenerator.util.IOUtils;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.PsiClassImpl;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.FilenameIndex;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.apache.commons.io.IOUtils.writeLines;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 21:50
 */
public class OnePojoInfoHelper {

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
        PsiField[] allFields = psiClass.getAllFields();
        List<PojoFieldInfo> fieldList = Lists.newArrayList();

        for (PsiField field : allFields) {
            if("serialVersionUID".equals(field.getName())){
                continue;
            }
            PojoFieldInfo fieldInfo = new PojoFieldInfo();
            PsiType type = field.getType();
            fieldInfo.setFieldName(field.getName());
            fieldInfo.setFieldClass(type.getPresentableText());
            fieldInfo.setAnnotations(Lists.newArrayList());
            fieldList.add(fieldInfo);
        }
        onePojoInfo.setPojoFieldInfos(fieldList);
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
        DirectoryConfig config = response.getDirectoryConfig();
        onePojoInfo.setFiles(Lists.newArrayList());
        for (FileType fileType : FileType.values()) {
            if(fileType == FileType.NONE){
                continue;
            }
            GeneratedFile file = new GeneratedFile();
            file.setFileType(fileType);
            String dir = config.getDirectoryMap().get(fileType);
            file.setFilePath(dir + response.getPathSplitter()+ onePojoInfo.getPojoName() + fileType.getSuffix());
            file.setFile(new File(file.getFilePath()));
            if(!file.getFile().exists()){
             file.getFile().getParentFile().mkdirs();
                try {
                    file.getFile().createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try{
                if(file.getFile().exists()){
                    file.setOldLines(Lists.newArrayList(IOUtils.readLines(file.getFile())));
                }else{
                    file.setOldLines(Lists.newArrayList());
                }

            }catch(Exception e){
                response.failure("","read " + dir +" failure");
            }
            file.setNewLines(Lists.newArrayList());
            onePojoInfo.getFiles().add(file);
        }
    }

    // todo
    public static BaseResponse flushFiles(@NotNull OnePojoInfo onePojoInfo, GenCodeResponse response){

        try{
            for (GeneratedFile generatedFile : onePojoInfo.getFiles()) {
                System.out.println("wirte linese");
                writeLines(generatedFile.getNewLines(), "\n", new FileOutputStream(generatedFile.getFile()));
            }
            return response;
        }catch(Exception e){
            return response.failure("","flush file error");
        }

    }
}
