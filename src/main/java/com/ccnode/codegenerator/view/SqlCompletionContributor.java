package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.util.PsiClassUtil;
import com.ccnode.codegenerator.util.PsiElementUtil;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    private static List<String> textEndList = new ArrayList<String>() {{
        add("find");
        add("update");
        add("and");
        add("by");
    }};


    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context) {
        //todo maybe need to add the method to check.
    }

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
        PsiClass containingClass = PsiElementUtil.getContainingClass(originalPosition);
        if (!containingClass.isInterface()) {
            return;
        }
        String text = originalPosition.getText();
        if (text.startsWith("find") || text.startsWith("update") || text.startsWith("delete")) {
            //go tell them to choose.
            //todo could use like when there. why after press tab can't show with more?
//            get pojo class from it.
            PsiClass pojoClass = PsiClassUtil.getPojoClass(containingClass);
            if (pojoClass == null) {
                return;
            }
            List<String> strings = PsiClassUtil.extractProps(pojoClass);
            List<String> formatProps = new ArrayList<String>();
            for (String s : strings) {
                formatProps.add(s.substring(0, 1).toUpperCase() + s.substring(1));
            }
            String lower = text.toLowerCase();
            for (String end : textEndList) {
                if (lower.endsWith(end)) {
                    //add formated prop to recommend list.
                    for (String prop : formatProps) {
                        LookupElementBuilder builder = LookupElementBuilder.create(text + prop);
                        result.addElement(builder);
                    }
                }
            }
        }
    }
}
