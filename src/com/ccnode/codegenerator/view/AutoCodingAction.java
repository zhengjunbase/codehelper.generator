package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.pojo.PojoLine;
import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.ccnode.codegenerator.util.IOUtils;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.PsiClassImpl;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class AutoCodingAction extends AnAction {

    public final static String SPACE = " ";

    public void actionPerformed(AnActionEvent event) {
        try {
            ApplicationManager.getApplication().saveAll();
            VirtualFileManager.getInstance().syncRefresh();
            PsiElement data1 = event.getData(LangDataKeys.PSI_ELEMENT);
            Editor editor = event.getData(LangDataKeys.EDITOR);
            CaretModel caretModel = editor.getCaretModel();
            LogicalPosition oldLogicPos = caretModel.getLogicalPosition();
            PsiFile currentFile = event.getData(LangDataKeys.PSI_FILE);
            String text = currentFile.getText();
            List<String> lines = Splitter.on("\n").splitToList(text);
            PojoLine pojo = getCursorLine(lines, oldLogicPos);
            PsiDirectory containingDirectory = currentFile.getContainingDirectory();
            Document document = PsiDocumentManager.getInstance(event.getProject()).getDocument(currentFile);
            String dir = containingDirectory.getVirtualFile().getCanonicalPath() + GenCodeResponseHelper.getPathSplitter() + currentFile.getName();
            PsiElement element = currentFile.findElementAt(editor.getCaretModel().getOffset());
            if (pojo != null) {
                LogicalPosition newPosition = new LogicalPosition(pojo.getLineNumber(), pojo.getVariablePos());
                caretModel.moveToLogicalPosition(newPosition);
                PsiElement data = event.getData(LangDataKeys.PSI_ELEMENT);
                if (data instanceof PsiClassImpl) {
                    List<String> setter = getSetter(data, pojo);
                    int offset = caretModel.getOffset();
                    Integer trueOffSet = getOffset(pojo, dir);
                    if(trueOffSet != 0){
                       offset = trueOffSet;
                    }
                    addSetters(setter, document,offset, pojo,event.getProject());
                    VirtualFileManager.getInstance().syncRefresh();
                    ApplicationManager.getApplication().saveAll();
                }
            }



        } catch (Throwable ignored) {

        }finally {

        }

    }

    private void addSetters(List<String> setter, Document document,int offset, PojoLine pojo,Project project) {

         String line = StringUtils.EMPTY;
        for (String s : setter) {
            line += s;
            line +=  "\n";
        }
        final String insert = line;
        ApplicationManager.getApplication().runWriteAction(new DocumentRunnable(document, null) {
            @Override
            public void run() {
                CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            document.insertString(offset,insert);
                        } catch (Exception ignored) {
                        }
                    }
                }, setter.get(0), document);
            }
        });

    }


    private Integer getOffset( PojoLine pojo, String path) {
        File file = new File(path);
        Integer ret = 0;
        try{
            if(!file.exists()){
                return null;
            }
            List<String> oldLine = IOUtils.readLines(file);
            for (String line: oldLine) {
                ret += line.length() + 1;
                if(line.equals(pojo.getRawLine())){
                    return ret;
                }
            }
        }catch(Throwable ignored){

        }
        return 0;


    }

    private List<String> getSetter(PsiElement data, PojoLine pojoLine) {
        List<String> retList = Lists.newArrayList();

        PsiClassImpl clazz = (PsiClassImpl) data;
        PsiMethod[] allMethods = clazz.getAllMethods();
        String linePreffix = pojoLine.getPreWhiteSpace() + pojoLine.getVariableName() + ".";
        String lineSuffix = "();";
        System.out.println(pojoLine.getRawLine());
        if (allMethods != null && allMethods.length > 0) {
            for (PsiMethod allMethod : allMethods) {
                if (allMethod.getText().contains(" static ")) {
                    continue;
                }
                if(allMethod.getText().contains(" set")){
                    retList.add(linePreffix + allMethod.getName() + lineSuffix);
                    System.out.println(linePreffix + allMethod.getName() + lineSuffix);
                }
            }
        }
        return retList;
    }

    @Nullable
    private PojoLine getCursorLine(List<String> lines, LogicalPosition oldLogicPos) {
        for (int i = oldLogicPos.line; i >= 0; i--) {
            String line = lines.get(i);
            PojoLine validPojoName = getValidPojoName(line, i);
            if (validPojoName != null) {
                return validPojoName;
            }
        }
        PojoLine pojoLine = new PojoLine();
        return null;

    }

    @Nullable
    private PojoLine getValidPojoName(String line, Integer lineNumber) {
        String copy = line;
        if (StringUtils.isBlank(copy)) {
            return null;
        }
        copy = copy.replace("=", " = ");
        copy = copy.replace("(", " ( ");
        copy = copy.replace(")", " ) ");
        copy = copy.replace(";", " ; ");
        List<String> splits = Splitter.on(SPACE).trimResults().omitEmptyStrings().splitToList(copy);
        if(splits.size() < 8){
            return null;
        }
        if (splits.get(0).equals(splits.get(4)) && splits.get(2).equals("=") && splits.get(3).equals("new") && splits
                .get(5).equals("(") && splits.get(6).equals(")") && splits.get(7).equals(";")) {
            PojoLine pojoLine = new PojoLine();
            pojoLine.setClassName(splits.get(0));
            pojoLine.setVariableName(splits.get(1));
            pojoLine.setLineNumber(lineNumber);
            pojoLine.setRawLine(line.toString());
            pojoLine.setVariablePos(line.indexOf(pojoLine.getClassName()) + 1);
            pojoLine.setPreWhiteSpace(line.substring(0, pojoLine.getVariablePos() - 1));
            return pojoLine;
        }
        return null;
    }
}