package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.genCode.GenCodeService;
import com.ccnode.codegenerator.pojo.GenCodeRequest;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.search.FilenameIndex;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

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
        @Nullable String projectPath = project.getBasePath();
//        System.out.println(project.getName());
//        System.out.println(project.getBaseDir());
//        System.out.println(project.getBasePath());
//        System.out.println(project.getName());
//        String projectPath = project.getBasePath();
        if(projectPath == null){
            projectPath = StringUtils.EMPTY;
        }
//        for (String s : FilenameIndex.getAllFilenames(project)) {
//            if(s.endsWith("java")){
//
//                System.out.println("filename: " +s);
//            }
//        }

//        PsiFile[] filesByName1 = FilenameIndex.getFilesByName(project, "FlightSegmentInfo.java", new EverythingGlobalScope(project));
//        if(filesByName1.length > 0){
//             System.out.println("dir: " +filesByName1[0].getName());
//            System.out.println("dir: " +filesByName1[0].getFileType());
//            System.out.println("dir: " +filesByName1[0].getContainingDirectory());
//            System.out.println("dir: " +filesByName1[0].getClass());
//        }
//        PsiFile[] filesByName2= FilenameIndex.getFilesByName(project, "FlightSegmentInfo", new EverythingGlobalScope(project));
//        if(filesByName2.length > 0){
//             System.out.println("dir: " +filesByName2[0].getName());
//            System.out.println("dir: " +filesByName2[0].getFileType());
//            System.out.println("dir: " +filesByName2[0].getContainingDirectory());
//        }
//        PsiFile psiFile = filesByName1[0];
//        PsiElement firstChild = psiFile.getFirstChild();
//        while (firstChild.getNextSibling() != null){
//            firstChild = firstChild.getNextSibling();
//            System.out.println(firstChild.getClass());
//            if(firstChild instanceof PsiClassImpl){
//                PsiClassImpl imp = (PsiClassImpl) firstChild;
//                PsiField[] allFields = imp.getAllFields();
//                for (PsiField allField : allFields) {
//                    System.out.println(allField.getName());
//                    System.out.println(allField.getNameIdentifier());
//                    System.out.println(allField.getContext());
//                    System.out.println(allField.getClass());
//                    System.out.println(allField.getClass().getClass());
//                    System.out.println(allField.getClass().getName());
//                    PsiType type = allField.getType();
//                    System.out.println(type.getClass());
//                    System.out.println(type.getSuperTypes());
//                    System.out.println(type.getPresentableText());
//                }
//            }
//        }
//        ClassLoader cl = ClassLoader.getSystemClassLoader();
//
//        URL[] urls = ((URLClassLoader)cl).getURLs();
//
//        for(URL url: urls){
//        	System.out.println(url.getFile());
//        }
//        System.out.println("fucks");

        GenCodeRequest request;
        GenCodeResponse genCodeResponse = new GenCodeResponse();
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
        Messages.showMessageDialog(project, genCodeResponse.getCode() + genCodeResponse.getMsg() + "Hello, "  + "!\n I am glad to see you.", "Information", Messages.getInformationIcon());

    }
}