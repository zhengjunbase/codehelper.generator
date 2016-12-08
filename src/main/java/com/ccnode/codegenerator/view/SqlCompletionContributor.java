package com.ccnode.codegenerator.view;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bruce.ge on 2016/12/8.
 */
public class SqlCompletionContributor extends CompletionContributor {
//    public SqlCompletionContributor() {
//        extend(CompletionType.BASIC, PsiJavaPatterns.psiElement().afterLeaf("find"), new CompletionProvider<CompletionParameters>() {
//            @Override
//            protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
//                //do something in it.
//                PsiElement position = parameters.getPosition();
//
//                PsiElement parent = position.getParent();
//                if (parent instanceof PsiMethod) {
//                    //do something in it.
//                    PsiMethod method = (PsiMethod) parent;
//                    PsiClass containingClass = method.getContainingClass();
//                    if (!containingClass.isInterface()) {
//                        return;
//                    }
//                    String name = method.getName();
//
//                }
//            }
//        });
//    }


    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (parameters.getCompletionType() != CompletionType.BASIC) {
            return;
        }
        PsiElement element = parameters.getPosition();
        PsiElement originalPosition = parameters.getOriginalPosition();
        PsiFile topLevelFile = InjectedLanguageUtil.getTopLevelFile(element);
        if (!(topLevelFile instanceof PsiJavaFile)) {
            return;
        }
        PsiJavaFile javaFile = (PsiJavaFile) topLevelFile;
        PsiClass[] classes = javaFile.getClasses();
        for (PsiClass psiClass : classes) {
            if (!psiClass.isInterface()) {
                return;
            }
        }
        String text = originalPosition.getText();
        if (text.startsWith("find") || text.startsWith("update")) {
            //go tell them to choose.
            LookupElementBuilder builder = LookupElementBuilder.create("findOrder");
            result.addElement(builder);
        }
    }


}
