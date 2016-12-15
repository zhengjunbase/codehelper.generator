package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.genCode.GenCodeService;
import com.ccnode.codegenerator.genCode.UserConfigService;
import com.ccnode.codegenerator.pojo.AltInsertInfo;
import com.ccnode.codegenerator.pojo.ChangeInfo;
import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.ccnode.codegenerator.service.SendToServerService;
import com.ccnode.codegenerator.service.pojo.GenCodeServerRequest;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.PojoUtil;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.generation.OverrideImplementUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/9.
 */
public class GenCodeUsingAltHandler implements CodeInsightActionHandler {

    private final static Logger LOGGER = LoggerWrapper.getLogger(GenCodeAction.class);

    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (!CodeInsightUtilBase.prepareEditorForWrite(editor)) return;
        if (!FileDocumentManager.getInstance().requestWriting(editor.getDocument(), project)) {
            return;
        }
        final PsiClass aClass = OverrideImplementUtil.getContextClass(project, editor, file, false);
        Module moduleForFile = ModuleUtilCore.findModuleForPsiElement(aClass);
        String modulePath = moduleForFile.getModuleFilePath();
        Path parent = Paths.get(modulePath).getParent();
        VirtualFileManager.getInstance().syncRefresh();
        ApplicationManager.getApplication().saveAll();
        if (project == null) {
            return;
        }
        String projectPath = parent.toString();
        UserConfigService.loadUserConfigNew(project, moduleForFile);
        if (projectPath == null) {
            projectPath = StringUtils.EMPTY;
        }
        GenCodeResponse genCodeResponse = new GenCodeResponse();
        GenCodeResponseHelper.setResponse(genCodeResponse);
        try {
            GenCodeRequest request = new GenCodeRequest(Lists.newArrayList(), projectPath,
                    System.getProperty("file.separator"));
            AltInsertInfo insertInfo = new AltInsertInfo();
            insertInfo.setSrcClass(aClass);
            insertInfo.setPath(file.getVirtualFile().getPath());
            request.setInfo(insertInfo);
            request.setProject(project);
            genCodeResponse = GenCodeService.genCode(request);
            VirtualFileManager.getInstance().syncRefresh();
            LoggerWrapper.saveAllLogs(projectPath);
//            if(!SettingService.showDonateBtn()){
            Messages.showMessageDialog(project, buildEffectRowMsg(genCodeResponse), genCodeResponse.getStatus(), null);
//            }else{
//                int result = Messages.showOkCancelDialog(project, buildEffectRowMsg(genCodeResponse), genCodeResponse.getStatus() ,"Donate", "OK", null);
//                if(result != 2){
//                    BrowserLauncher.getInstance().browse(UrlManager.getDonateClickUrl() , WebBrowserManager.getInstance().getFirstActiveBrowser());
//                    SettingService.setDonated();
//                }
//            }
        } catch (Throwable e) {
            LOGGER.error("actionPerformed error", e);
            genCodeResponse.setThrowable(e);
        } finally {
            GenCodeServerRequest request = SendToServerService.buildGenCodeRequest(genCodeResponse);
            SendToServerService.post(project, request);
        }
        VirtualFileManager.getInstance().syncRefresh();
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    private static String buildEffectRowMsg(GenCodeResponse response) {
        List<ChangeInfo> newFiles = response.getNewFiles();
        List<ChangeInfo> updateFiles = response.getUpdateFiles();
        List<String> msgList = Lists.newArrayList();
        if (response.checkSuccess()) {
            Integer affectRows = getAffectRows(newFiles);
            affectRows += getAffectRows(updateFiles);
            if (newFiles.isEmpty() && affectRows.equals(0)) {
                return "No File Generated Or Updated.";
            }
            if (!newFiles.isEmpty()) {
                msgList.add(" ");
            }
            for (ChangeInfo newFile : newFiles) {
                msgList.add("new File:" + "  " + newFile.getFileName());
            }
            if (!updateFiles.isEmpty()) {
                msgList.add("");
            }
            for (ChangeInfo updated : updateFiles) {
                if (updated.getAffectRow() > 0) {
                    msgList.add("updated:" + "  " + updated.getFileName());
                }
            }
        } else {
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
            if (newFile.getAffectRow() != null) {
                affectRow += newFile.getAffectRow();
            }
        }
        return affectRow;
    }
}
