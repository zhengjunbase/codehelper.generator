package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.enums.UrlManager;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.browsers.WebBrowserManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class ShowLearnMoreAction extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        BrowserLauncher.getInstance().browse(UrlManager.getMainPage() , WebBrowserManager.getInstance().getFirstActiveBrowser());
    }
}