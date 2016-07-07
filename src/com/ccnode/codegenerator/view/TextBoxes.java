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
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class TextBoxes extends AnAction {
    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public TextBoxes() {
        // Set the menu item name.
        super("Text _Boxes");
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        if(project == null){
            return;
        }
        @Nullable String projectPath = project.getBasePath();
        if(projectPath == null){
            projectPath = StringUtils.EMPTY;
        }

        GenCodeRequest request;
        GenCodeResponse genCodeResponse = new GenCodeResponse();
        Date oldTime;
        String s = StringUtils.EMPTY;
        SettingService service = ServiceManager.getService(SettingService.class);
        SettingDto state = service.getState();

        try{
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            String a = sb.toString();
            System.out.println(a);

        }catch(Exception e){
            System.out.println(e);
        }



        try{
            request = new GenCodeRequest(Lists.newArrayList(),projectPath,"/");
            request.setProject(project);
            genCodeResponse = GenCodeService.genCode(request);
        }catch(Exception e){
            e.printStackTrace();
            genCodeResponse.setMsg(e.getMessage());
        }
//        String txt= Messages
//                .showInputDialog(project, projectPath + "Insdfsput pojos splits with comma?", "Input Pojos", Messages.getQuestionIcon());
        Messages.showMessageDialog(project, genCodeResponse.getCode() + genCodeResponse.getMsg() + s +"Hello, "  + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());

    }
}