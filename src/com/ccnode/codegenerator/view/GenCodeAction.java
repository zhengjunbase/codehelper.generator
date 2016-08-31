package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.enums.UrlManager;
import com.ccnode.codegenerator.genCode.GenCodeService;
import com.ccnode.codegenerator.pojo.ChangeInfo;
import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.storage.SettingDto;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.google.common.collect.Lists;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.browsers.WebBrowserManager;
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
import java.util.List;

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
            LoggerWrapper.saveAllLogs(genCodeResponse);
        }catch(Exception e){
            e.printStackTrace();
            genCodeResponse.setMsg(e.getMessage());
        }
        if(SettingService.getInstance().canUsePremium()){
            Messages.showMessageDialog(project, buildEffectRowMsg(genCodeResponse), "-------"+genCodeResponse.getStatus() +"-------",null);
        }else{
            int result = Messages.showOkCancelDialog(project, buildEffectRowMsg(genCodeResponse),
                    "-------" + genCodeResponse.getStatus() + "-------","OK","Buy Premium", null);
            if(result == 2){
                BrowserLauncher.getInstance().browse(UrlManager.PREMIUM_URL, WebBrowserManager.getInstance().getFirstActiveBrowser());
            }
        }
        ApplicationManager.getApplication().saveAll();
        VirtualFileManager.getInstance().syncRefresh();
    }

    private static String buildEffectRowMsg(GenCodeResponse response){
        List<ChangeInfo> newFiles = response.getNewFiles();
        List<ChangeInfo> updateFiles = response.getUpdateFiles();
        List<String> msgList = Lists.newArrayList();
        if(response.checkSuccess()){
            if(newFiles.size() > 0){
                msgList.add(" ");
            }
            for (ChangeInfo newFile : newFiles) {
                msgList.add("       new file:"+ "\t\t" + newFile.getFileName());
            }
            if(updateFiles.size() > 0){
                msgList.add("");
            }
            for (ChangeInfo updated : updateFiles) {
                if(updated.getAffectRow() > 0){
                    msgList.add("   updated:"+ "\t\t" + updated.getFileName());
                }
            }
        }else{
            msgList.add(response.getMsg());
        }
        String ret = StringUtils.EMPTY;
        for (String msg : msgList) {
            ret += msg;
            ret += "\n";
        }
        return ret.trim();

    }
}