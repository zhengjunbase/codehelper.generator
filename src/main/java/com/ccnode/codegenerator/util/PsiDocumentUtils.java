package com.ccnode.codegenerator.util;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiImportStatement;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiPackageStatement;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by bruce.ge on 2016/12/23.
 */
public class PsiDocumentUtils {

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


    public static void addImportToFile(PsiDocumentManager psiDocumentManager, PsiJavaFile containingFile, Document document, Set<String> newImportList) {
        if (newImportList.size() > 0) {
            Iterator<String> iterator = newImportList.iterator();
            while (iterator.hasNext()) {
                String u = iterator.next();
                if (u.startsWith("java.lang")) {
                    iterator.remove();
                }
            }
        }

        if (newImportList.size() > 0) {
            PsiJavaFile javaFile = containingFile;
            PsiImportStatement[] importStatements = javaFile.getImportList().getImportStatements();
            Set<String> containedSet = new HashSet<>();
            for (PsiImportStatement s : importStatements) {
                containedSet.add(s.getQualifiedName());
            }
            StringBuilder newImportText = new StringBuilder();
            for (String newImport : newImportList) {
                if (!containedSet.contains(newImport)) {
                    newImportText.append("\nimport " + newImport + ";");
                }
            }
            PsiPackageStatement packageStatement = javaFile.getPackageStatement();
            int start = 0;
            if (packageStatement != null) {
                start = packageStatement.getTextLength() + packageStatement.getTextOffset();
            }
            final int textStart = start;
            String insertText = newImportText.toString();
            if (StringUtils.isNotBlank(insertText)) {
                WriteCommandAction.runWriteCommandAction(containingFile.getProject(), () -> {
                    document.insertString(textStart, insertText);
                    PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, document);
                });
            }
        }
    }




}
