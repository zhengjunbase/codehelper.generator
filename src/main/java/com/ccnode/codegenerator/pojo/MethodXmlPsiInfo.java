package com.ccnode.codegenerator.pojo;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

/**
 * Created by bruce.ge on 2016/12/12.
 */
public class MethodXmlPsiInfo {
    private PsiMethod method;

    private String methodName;

    private String returnClassName;

    private PsiClass pojoClass;

    public PsiClass getPojoClass() {
        return pojoClass;
    }

    public void setPojoClass(PsiClass pojoClass) {
        this.pojoClass = pojoClass;
    }

    public PsiMethod getMethod() {
        return method;
    }

    public void setMethod(PsiMethod method) {
        this.method = method;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnClassName() {
        return returnClassName;
    }

    public void setReturnClassName(String returnClassName) {
        this.returnClassName = returnClassName;
    }
}
