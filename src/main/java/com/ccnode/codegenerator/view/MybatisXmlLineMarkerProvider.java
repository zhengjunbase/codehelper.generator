package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.util.IconUtils;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MybatisXmlLineMarkerProvider extends RelatedItemLineMarkerProvider {

    private static Set<String> tagNameSet = new HashSet<String>() {{
        add("select");
        add("insert");
        add("update");
        add("delete");
    }};

    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        if (!(element instanceof XmlTag))
            return;
        PsiFile psiFile = element.getContainingFile();
        if (!(psiFile instanceof XmlFile))
            return;
        XmlFile xmlFile = (XmlFile) psiFile;
        if (!xmlFile.getRootTag().getName().equals("mapper")) {
            return;
        }
        XmlTag tag = (XmlTag) element;
        if (!tagNameSet.contains(tag.getName())) {
            return;
        }
        XmlAttribute namespaceAttribute = xmlFile.getRootTag().getAttribute("namespace");
        if (namespaceAttribute == null || namespaceAttribute.getValue() == null) {
            return;
        }
        String namespace = namespaceAttribute.getValue();
        XmlAttribute id = tag.getAttribute("id");
        if (id == null || id.getValue() == null) {
            return;
        }
        String[] split = namespace.split("\\.");
        String className = split[split.length - 1];
        Module moduleForPsiElement = ModuleUtilCore.findModuleForPsiElement(element);
        if (moduleForPsiElement == null) {
            return;
        }
        PsiClass[] classesByName = PsiShortNamesCache.getInstance(element.getProject()).getClassesByName(className, GlobalSearchScope.moduleScope(moduleForPsiElement));
        PsiClass realClass = null;
        for (PsiClass psiClass : classesByName) {
            if (psiClass.isInterface() && psiClass.getQualifiedName().equals(namespace)) {
                //
                realClass = psiClass;
                break;
            }
        }
        if (realClass == null) {
            return;
        }
        String xmlMethodName = id.getValue();
        PsiElement findedMethod = null;
        PsiMethod[] allMethods = realClass.getAllMethods();
        for (PsiMethod classMethod : allMethods) {
            if (xmlMethodName.equals(classMethod.getName())) {
                findedMethod = classMethod;
                break;
            }
        }
        if (findedMethod == null) {
            return;
        }
        result.add(NavigationGutterIconBuilder.create(IconUtils.useXmlIcon()).setAlignment(GutterIconRenderer.Alignment.CENTER)
                .setTarget(findedMethod).setTooltipTitle("navigation to mapper class").createLineMarkerInfo(element));


        //means we find the class. then get it all method and get it.
    }
}
