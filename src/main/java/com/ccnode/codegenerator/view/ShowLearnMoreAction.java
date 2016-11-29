package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.enums.UrlManager;
import com.ccnode.codegenerator.genCode.GenCodeService;
import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.storage.SettingDto;
import com.ccnode.codegenerator.storage.SettingService;
import com.google.common.collect.Lists;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.browsers.WebBrowser;
import com.intellij.ide.browsers.WebBrowserManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class ShowLearnMoreAction extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        BrowserLauncher.getInstance().browse(UrlManager.getGeneratorUrl() , WebBrowserManager.getInstance().getFirstActiveBrowser());
    }
}