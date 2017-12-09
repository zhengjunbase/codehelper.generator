package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.genCode.genFind.ParseJpaResponse;
import com.ccnode.codegenerator.genCode.genFind.ParseJpaStrService;
import com.ccnode.codegenerator.pojo.OnePojoInfo;
import com.ccnode.codegenerator.pojoHelper.OnePojoInfoHelper;
import com.ccnode.codegenerator.view.MethodNameCompletionContributor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaCodeReferenceElement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackageStatement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.compiled.ClsFieldImpl;
import com.intellij.psi.xml.XmlFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 */
public class PsiDocumentUtils {


    public static void appendMethodToClass(Project project, PsiClass psiClass, String text){

    }

    public static void appendMethodToXml(Project project, PsiClass psiClass, String text){

    }
    public static void commitAndSaveDocument (Project project, Document document, final TextRange textRange, String newText){
        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                document.replaceString(textRange.getStartOffset(), textRange.getEndOffset(), newText);
                commitAndSaveDocument(psiDocumentManager, document);
            }
        };
         WriteCommandAction.runWriteCommandAction(project, runnable);
    }

    public static void commitAndSaveDocument(PsiDocumentManager psiDocumentManager, Document document) {
        if (document != null) {
            psiDocumentManager.doPostponedOperationsAndUnblockDocument(document);
            psiDocumentManager.commitDocument(document);
            FileDocumentManager.getInstance().saveDocument(document);
        }
    }


    public static OnePojoInfo smartParseFromPsiElement(@NotNull Project project, @NotNull PsiElement element){

        if(element == null){
            return null;
        }
        PsiFile containingFile = element.getContainingFile();
        if(containingFile == null){
            return null;
        }
        if(containingFile instanceof XmlFile){
            return parseFromXml(project, element);
        }else if(containingFile instanceof PsiJavaFile){
            return parseFromJavaFile(project,element);
        }else{
            return null;
        }

    }

    private static OnePojoInfo parseFromJavaFile(Project project, PsiElement element) {
        PsiElement parent = element.getParent();
        if (parent instanceof PsiMethod) {
            return null;
        }
        PsiClass daoClass = PsiElementUtil.getContainingClass(element);
        String qualifiedName = daoClass.getQualifiedName();
        if(daoClass == null || !daoClass.isInterface()){
            return null;
        }
        String pojoName = OnePojoInfoHelper.parsePojoNameFromDaoClass(daoClass);
        if(StringUtils.isBlank(pojoName)){
            return null;
        }
        OnePojoInfo ret = new OnePojoInfo();
        ret.setPojoName(pojoName);
        ret.setDaoClass(daoClass);
        XmlFile xmlFile = OnePojoInfoHelper.findXmlFileByDaoQualifiedClassName(project, qualifiedName);
        ret.setXmlFile(xmlFile);
        return ret;
    }

    private static OnePojoInfo parseFromXml(Project project, PsiElement element) {
        return null;
    }

    private static PsiElement findLastMatchedElement(PsiElement element) {
        PsiElement prevSibling = element.getPrevSibling();
        while (prevSibling != null && isIgnoreText(prevSibling.getText())) {
            prevSibling = prevSibling.getPrevSibling();
        }
        if (prevSibling != null) {
            String lowerCase = prevSibling.getText().toLowerCase();
            if (MethodNameCompletionContributor.checkValidTextStarter(lowerCase)) {
                return prevSibling;
            }
        }
        return null;
    }


    private static boolean isIgnoreText(String text) {
        return (text.equals("")) || (text.equals("\n")) || text.equals(" ");
    }





}
