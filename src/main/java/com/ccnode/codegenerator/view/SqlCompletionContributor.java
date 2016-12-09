package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.util.PsiElementUtil;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
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
            String lower = text.toLowerCase();
            for(String end:textEndList){
                if(lower.endsWith(end)){
                    LookupElementBuilder builder = LookupElementBuilder.create(text + "order");
                    result.addElement(builder);
                }
            }
        }
    }
}
