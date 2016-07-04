package com.ccnode.codegenerator.pojo;

import com.ccnode.codegenerator.util.GenCodeConfig;
import com.intellij.psi.impl.source.PsiClassImpl;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/17 19:57
 */
public class OnePojoInfo {

    List<GeneratedFile> files;
    List<PojoFieldInfo> pojoFieldInfos;
    GenCodeConfig genCodeConfig;
    DirectoryConfig directoryConfig;
    PsiClassImpl psiClass;
    Class pojoClass;
    String pojoName;
    String fullPojoPath;
    String pojoPackage;
    String daoPackage;
    String servicePackage;
    String pojoClassSimpleName;

    public String getPojoName() {
        return pojoName;
    }

    public void setPojoName(String pojoName) {
        this.pojoName = pojoName;
    }

    public DirectoryConfig getDirectoryConfig() {
        return directoryConfig;
    }

    public void setDirectoryConfig(DirectoryConfig directoryConfig) {
        this.directoryConfig = directoryConfig;
    }

    public List<GeneratedFile> getFiles() {
        return files;
    }

    public void setFiles(List<GeneratedFile> files) {
        this.files = files;
    }

    public List<PojoFieldInfo> getPojoFieldInfos() {
        return pojoFieldInfos;
    }

    public void setPojoFieldInfos(List<PojoFieldInfo> pojoFieldInfos) {
        this.pojoFieldInfos = pojoFieldInfos;
    }

    public GenCodeConfig getGenCodeConfig() {
        return genCodeConfig;
    }

    public void setGenCodeConfig(GenCodeConfig genCodeConfig) {
        this.genCodeConfig = genCodeConfig;
    }

    public Class getPojoClass() {
        return pojoClass;
    }

    public void setPojoClass(@Nullable Class pojoClass) {
        this.pojoClass = pojoClass;
    }

    public String getFullPojoPath() {
        return fullPojoPath;
    }

    public void setFullPojoPath(String fullPojoPath) {
        this.fullPojoPath = fullPojoPath;
    }

    public String getPojoPackage() {
        return pojoPackage;
    }

    public void setPojoPackage(String pojoPackage) {
        this.pojoPackage = pojoPackage;
    }

    public String getDaoPackage() {
        return daoPackage;
    }

    public void setDaoPackage(String daoPackage) {
        this.daoPackage = daoPackage;
    }

    public String getServicePackage() {
        return servicePackage;
    }

    public void setServicePackage(String servicePackage) {
        this.servicePackage = servicePackage;
    }

    public String getPojoClassSimpleName() {
        return pojoClassSimpleName;
    }

    public void setPojoClassSimpleName(String pojoClassSimpleName) {
        this.pojoClassSimpleName = pojoClassSimpleName;
    }

    public PsiClassImpl getPsiClass() {
        return psiClass;
    }

    public void setPsiClass(PsiClassImpl psiClass) {
        this.psiClass = psiClass;
    }
}
