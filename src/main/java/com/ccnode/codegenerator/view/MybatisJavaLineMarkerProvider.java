package com.ccnode.codegenerator.view;

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo;
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by bruce.ge on 2016/12/8.
 */
public class MybatisJavaLineMarkerProvider extends RelatedItemLineMarkerProvider {
    @Override
    protected void collectNavigationMarkers(@NotNull PsiElement element, Collection<? super RelatedItemLineMarkerInfo> result) {
        //first make the example pass, then change it with what's wanted.
        if (element instanceof PsiLiteralExpression) {
            PsiLiteralExpression psiLiteralExpression = (PsiLiteralExpression) element;
            String value = psiLiteralExpression.getValue() instanceof String ? (String) psiLiteralExpression.getValue() : null;
            if (value != null && value.startsWith("simple" + ":")) {
                Project project = element.getProject();
                PsiFile[] filesByName = PsiShortNamesCache.getInstance(project).getFilesByName("CommentPODao.xml");
                PsiFile psiFile = filesByName[0];
                result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementedMethod).setAlignment(GutterIconRenderer.Alignment.CENTER)
                        .setTarget(psiFile).setTooltipTitle("navigation to mapper xml").createLineMarkerInfo(element));
            }
        }
        if (element instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) element;
            PsiClass containingClass = method.getContainingClass();
            if (!containingClass.isInterface()) {
                return;
            }
            String qualifiedName = containingClass.getQualifiedName();
            if (qualifiedName.endsWith("Dao")) {
                Project project = element.getProject();
                PsiFile[] filesByName = PsiShortNamesCache.getInstance(project).getFilesByName(containingClass.getName() + ".xml");
                if (filesByName.length == 0) {
                    return;
                }
                PsiElement methodElement = null;
                for (PsiFile file : filesByName) {
                    if (file instanceof XmlFile) {
                        XmlFile xmlFile = (XmlFile) file;
                        XmlAttribute namespace = xmlFile.getRootTag().getAttribute("namespace");
                        if (namespace == null || !namespace.getValue().equals(qualifiedName)) {
                            continue;
                        }
                        //say we find the xml file.
                        XmlTag[] subTags = xmlFile.getRootTag().getSubTags();
                        if (subTags.length == 0) {
                            continue;
                        }
                        for (XmlTag tag : subTags) {
                            XmlAttribute id = tag.getAttribute("id");
                            if (id != null && id.getValue().equals(method.getName())) {
                                methodElement = tag;
                                break;
                            }
                        }
                        if (methodElement != null) {
                            break;
                        }
                    }
                }

                if (methodElement != null) {
                    result.add(NavigationGutterIconBuilder.create(AllIcons.Gutter.ImplementedMethod).setAlignment(GutterIconRenderer.Alignment.CENTER)
                            .setTarget(methodElement).setTooltipTitle("navigation to mapper xml").createLineMarkerInfo(element));
                }
            } else {
                return;
            }
            //只进行method的判断 进行控制 其他的不管
        }
    }
}
