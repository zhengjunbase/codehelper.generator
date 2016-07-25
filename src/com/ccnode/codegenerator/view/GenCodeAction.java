package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.genCode.GenCodeService;
import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.storage.SettingDto;
import com.ccnode.codegenerator.storage.SettingService;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.PlatformVirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class GenCodeAction extends AnAction {
    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public GenCodeAction() {
        // Set the menu item name.
        super("Text _Boxes");
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        VirtualFileManager.getInstance().syncRefresh();
        ApplicationManager.getApplication().saveAll();
        if(project == null){
            return;
        }
        @Nullable String projectPath = project.getBasePath();
        if(projectPath == null){
            projectPath = StringUtils.EMPTY;
        }

        GenCodeRequest request;
        GenCodeResponse genCodeResponse = new GenCodeResponse();

        try{
            request = new GenCodeRequest(Lists.newArrayList(),projectPath,System.getProperty("file.separator"));
            request.setProject(project);
            genCodeResponse = GenCodeService.genCode(request);
            VirtualFileManager.getInstance().syncRefresh();
        }catch(Exception e){
            e.printStackTrace();
            genCodeResponse.setMsg(e.getMessage());
        }
        Messages.showMessageDialog(project, genCodeResponse.getMsg(), genCodeResponse.getStatus(), Messages.getInformationIcon());
        ApplicationManager.getApplication().saveAll();
        VirtualFileManager.getInstance().syncRefresh();

    }
}