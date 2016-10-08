package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.enums.UrlManager;
import com.ccnode.codegenerator.genCode.GenCodeService;
import com.ccnode.codegenerator.pojo.ChangeInfo;
import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.ccnode.codegenerator.service.SendToServerService;
import com.ccnode.codegenerator.service.pojo.PostResponse;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.PojoUtil;
import com.google.common.collect.Lists;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.browsers.WebBrowserManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class GenCodeAction extends AnAction {

    private final static Logger LOGGER = LoggerWrapper.getLogger(GenCodeAction.class);

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
        GenCodeResponse genCodeResponse = new GenCodeResponse();
        GenCodeResponseHelper.setResponse(genCodeResponse);
        try{
            GenCodeRequest request = new GenCodeRequest(Lists.newArrayList(), projectPath,
                    System.getProperty("file.separator"));
            request.setProject(project);
            genCodeResponse = GenCodeService.genCode(request);
            VirtualFileManager.getInstance().syncRefresh();
            LoggerWrapper.saveAllLogs(genCodeResponse);
            if(SettingService.getInstance().canUsePremium()){
//                Messages.showDialog(project, buildEffectRowMsg(genCodeResponse), genCodeResponse.getStatus(), null);
                Messages.showMessageDialog(project, buildEffectRowMsg(genCodeResponse), genCodeResponse.getStatus(), null);
//                Messages.showOkCancelDialog(project, buildEffectRowMsg(genCodeResponse), genCodeResponse.getStatus() ,"OK","Share", null);

            }else{
                int result = Messages.showOkCancelDialog(project, buildEffectRowMsg(genCodeResponse), genCodeResponse.getStatus() ,"OK","Buy Premium", null);
                if(result == 2){
                    BrowserLauncher.getInstance().browse(UrlManager.PREMIUM_URL, WebBrowserManager.getInstance().getFirstActiveBrowser());
                }
            }
        }catch(Throwable e){
            LOGGER.error("actionPerformed error",e);
            genCodeResponse.setThrowable(e);
        }finally {
            SendToServerService.postToCheck(project, genCodeResponse);
            SendToServerService.postToServer(project, genCodeResponse);
        }
        ApplicationManager.getApplication().saveAll();
        VirtualFileManager.getInstance().syncRefresh();
    }

    private static String buildEffectRowMsg(GenCodeResponse response){
        List<ChangeInfo> newFiles = response.getNewFiles();
        List<ChangeInfo> updateFiles = response.getUpdateFiles();
        List<String> msgList = Lists.newArrayList();
        if(response.checkSuccess()){
            Integer affectRows = getAffectRows(newFiles);
            affectRows += getAffectRows(updateFiles);
            if(newFiles.isEmpty() && affectRows.equals(0)){
                return "No File Generated Or Updated.";
            }
            if(!newFiles.isEmpty()){
                msgList.add(" ");
            }
            for (ChangeInfo newFile : newFiles) {
                msgList.add("       new file:"+ "\t\t" + newFile.getFileName());
            }
            if(!updateFiles.isEmpty()){
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

    private static Integer getAffectRows(List<ChangeInfo> newFiles) {
        newFiles = PojoUtil.avoidEmptyList(newFiles);
        Integer affectRow = 0;
        for (ChangeInfo newFile : newFiles) {
            if(newFile.getAffectRow() != null){
                affectRow += newFile.getAffectRow();
            }
        }
        return affectRow;
    }
}