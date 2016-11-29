package com.ccnode.codegenerator.util;

import com.ccnode.codegenerator.storage.SettingDto;
import com.ccnode.codegenerator.storage.SettingService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.DocumentRunnable;
import com.intellij.openapi.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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





    public static void replaceString(Project project, Document document, String newString, int offset,int length) {
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
