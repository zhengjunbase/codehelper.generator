package com.ccnode.codegenerator.util;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTypesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/9.
 */
public class PsiClassUtil {
    public static List<String> extractProps(PsiClass pojoClass) {
        PsiField[] allFields = pojoClass.getAllFields();
        List<String> props = new ArrayList<String>();
        for (PsiField psiField : allFields) {
            if (psiField.hasModifierProperty("private") && !psiField.hasModifierProperty("static")) {
                props.add(psiField.getName());
            }
        }
        return props;
    }

    public static PsiMethod getAddMethod(PsiClass srcClass) {
        PsiMethod[] methods = srcClass.getMethods();
        for (PsiMethod classMethod : methods) {
            String name = classMethod.getName().toLowerCase();
            if (name.startsWith("insert") || name.startsWith("save") || name.startsWith("add")) {
                return classMethod;
            }
        }
        return null;
    }

    public static PsiClass getPojoClass(PsiClass srcClass) {
        PsiMethod addMethod = null;
        addMethod = getAddMethod(srcClass);
        if (addMethod != null) {
            PsiParameterList parameterList = addMethod.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            if (parameters.length == 1) {
                PsiType type = parameters[0].getType();
                PsiClass psiClass = PsiTypesUtil.getPsiClass(type);
                return psiClass;
                //try to get it from the class.
            }
        }
        return null;
    }
}
