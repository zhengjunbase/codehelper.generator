package com.ccnode.codegenerator.pojo;

import com.intellij.psi.PsiClass;

/**
 * Created by bruce.ge on 2016/12/9.
 */
public class AltInsertInfo {
    private String path;

    private PsiClass srcClass;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PsiClass getSrcClass() {
        return srcClass;
    }

    public void setSrcClass(PsiClass srcClass) {
        this.srcClass = srcClass;
    }
}
