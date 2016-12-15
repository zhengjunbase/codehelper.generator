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
        List<PsiMethod> methodsList = new ArrayList<PsiMethod>();
        for (PsiMethod classMethod : methods) {
            String name = classMethod.getName().toLowerCase();
            if (name.startsWith("insert") || name.startsWith("save") || name.startsWith("add")) {
                methodsList.add(classMethod);
            }
        }
        if (methodsList.size() == 0) {
            return null;
        } else {
            PsiMethod miniMethod = methodsList.get(0);
            for (int i = 1; i < methodsList.size(); i++) {
                if (methodsList.get(i).getName().length() < miniMethod.getName().length()) {
                    miniMethod = methodsList.get(i);
                }
            }
            return miniMethod;
        }
    }

    public static PsiClass getPojoClass(PsiClass srcClass) {
        PsiMethod addMethod = getAddMethod(srcClass);
        if (addMethod != null) {
            PsiParameterList parameterList = addMethod.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            if (parameters.length == 1) {
                PsiType type = parameters[0].getType();
                PsiClass psiClass = PsiTypesUtil.getPsiClass(type);
                PsiField[] allFields = psiClass.getAllFields();
                //// TODO: 2016/12/15 maybe need check if exist id property.
                if (allFields != null && allFields.length > 0) {
                    return psiClass;
                }
                //try to get it from the class.
            }
        }
        return null;
    }
}
