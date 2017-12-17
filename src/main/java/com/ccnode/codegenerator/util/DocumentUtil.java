package com.ccnode.codegenerator.util;

import com.google.common.base.Splitter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.DocumentRunnable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/10/20 09:57
 */
public class DocumentUtil {

    private final static Logger LOGGER = LoggerWrapper.getLogger(DocumentUtil.class);

    public static void insertString(Document document, String insertLine, int offset, Project project) {

        ApplicationManager.getApplication().runWriteAction(new DocumentRunnable(document, null) {
            @Override
            public void run() {
                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            document.insertString(offset, insertLine);
                        } catch (Exception ignored) {
                        }
                    }
                }, insertLine, document);
            }
        });

    }

    public static String getTextByLine(Document document, int lineIndex) {
        if (document == null) {
            return StringUtils.EMPTY;
        }
        int lineCount = document.getLineCount();
        if (lineCount > lineCount) {
            return StringUtils.EMPTY;
        }
        int start = document.getLineStartOffset(lineIndex);
        int end = document.getLineEndOffset(lineIndex);
        return document.getText(new TextRange(start, end));
    }

    public static Integer locateLineNumber(Document document, String text){
        String documentText = document.getText();
        List<String> strings = Splitter.on("\n").splitToList(documentText);
        int ret = 0;
        for (String string : strings) {
            if(string.contains(text)){
                return ret;
            }
            ret ++;
        }
        return -1;
    }

    public static void replaceString(Project project, Document document, String newString, int offset, int length) {
        ApplicationManager.getApplication().runWriteAction(new DocumentRunnable(document, null) {
            @Override
            public void run() {
                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            document.replaceString(offset, offset + length, newString);
                        } catch (Exception ignored) {
                        }
                    }
                }, "", document);
            }
        });
    }
}
