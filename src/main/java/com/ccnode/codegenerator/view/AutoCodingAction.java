package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.genCode.UserConfigService;
import com.ccnode.codegenerator.pojo.PojoLine;
import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.ccnode.codegenerator.pojoHelper.ProjectHelper;
import com.ccnode.codegenerator.pojoHelper.ServerRequestHelper;
import com.ccnode.codegenerator.service.SendToServerService;
import com.ccnode.codegenerator.service.pojo.AutoCodingRequest;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.DocumentUtil;
import com.ccnode.codegenerator.util.IOUtils;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsClassImpl;
import com.intellij.psi.impl.source.PsiClassImpl;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class AutoCodingAction extends AnAction {

    public final static String SPACE = " ";

    private final static Logger LOGGER = LoggerWrapper.getLogger(AutoCodingAction.class);


    public void actionPerformed(AnActionEvent event) {
        String projectPath = StringUtils.EMPTY;
        try {
            UserConfigService.loadUserConfig(event);
            projectPath = ProjectHelper.getProjectPath(event);
            Project project = event.getProject();
            Editor editor = event.getData(LangDataKeys.EDITOR);
            PsiFile currentFile = event.getData(LangDataKeys.PSI_FILE);
            CaretModel caretModel = editor.getCaretModel();
            LogicalPosition oldLogicPos = caretModel.getLogicalPosition();
            String text = currentFile.getText();
            List<String> lines = Splitter.on("\n").splitToList(text);
            PojoLine pojo = getCursorLine(lines, oldLogicPos);
            PsiDirectory containingDirectory = currentFile.getContainingDirectory();
//            HintManager.getInstance().showInformationHint(editor,"success");
            String dir = containingDirectory.getVirtualFile().getCanonicalPath() + GenCodeResponseHelper.getPathSplitter() + currentFile.getName();
            AutoCodingRequest request = new AutoCodingRequest();
            request.setRequestType("AutoCoding");
            request.setCodingType("Setter");
            ServerRequestHelper.fillCommonField(request);
            if (pojo != null) {
                request.setPojoName(pojo.getClassName());
                LogicalPosition newStatementPos = new LogicalPosition(pojo.getLineNumber() , pojo.getLineStartPos() + 1);
                LogicalPosition insertPos = new LogicalPosition(pojo.getLineNumber() + 1 , 0 );
                caretModel.moveToLogicalPosition(newStatementPos);
                PsiElement currentFileElement = event.getData(LangDataKeys.PSI_ELEMENT);
                if (currentFileElement instanceof PsiClassImpl || currentFileElement instanceof ClsClassImpl) {
                    //                    Integer trueOffSet = getOffset(pojo, dir);
                    //                    if(trueOffSet != 0){
                    //                       offset = trueOffSet;
                    //                    }
                    Document document = PsiDocumentManager.getInstance(event.getProject()).getDocument(currentFile);
                    caretModel.moveToLogicalPosition(insertPos);
                    Integer offset = caretModel.getOffset();
                    String insert = insertSetter(project, pojo, document, currentFileElement, offset);
                    request.setInsert(insert);
//                    SettingService.getSetting().setLastInsertPos(offset);
//                    SettingService.getSetting().setLastInsertLength(setter.length());
                }
            }
//            VirtualFileManager.getInstance().syncRefresh();
//            ApplicationManager.getApplication().saveAll();

            caretModel.moveToLogicalPosition(oldLogicPos);
            SendToServerService.post(project,request);
        } catch (Throwable ignored) {
            LOGGER.error("actionPerformed :{}", ignored);
        }finally {
            LoggerWrapper.saveAllLogs(projectPath);
            SettingService.updateLastRunTime();
        }

    }

    private String insertSetter(Project project, PojoLine pojo, Document document, PsiElement currentFileElement,
            Integer offset) {
        LOGGER.info("insertSetter");
        String withDefaultValue = genInsertLine(currentFileElement, pojo, true);
        String noDefaultValue = genInsertLine(currentFileElement, pojo, false);
        int endOffSet = offset + noDefaultValue.length();
        if(endOffSet >= document.getTextLength() + 1){
            LOGGER.info("insertSetter  insertString noDefaultValue:{},offset:{}", noDefaultValue, offset);
            DocumentUtil.insertString(document,noDefaultValue,offset, project);
            return noDefaultValue;
        }
        TextRange range = new TextRange(offset, offset + noDefaultValue.length());
        LOGGER.info("insertSetter textInRange:{}",document.getText(range));
        if(document.getText(range).contains(withDefaultValue)
                || withDefaultValue.contains(document.getText(range))){
            LOGGER.info("insertSetter replaceString withDefaultValue:{},offset:{},length:{}"
                    ,noDefaultValue, offset,withDefaultValue.length());
            DocumentUtil.replaceString(project,document, noDefaultValue,offset, withDefaultValue.length() );
            return noDefaultValue;
        }
        range = new TextRange(offset, offset + noDefaultValue.length());
        LOGGER.info("insertSetter textInRange:{}",document.getText(range));
        if(document.getText(range).contains(noDefaultValue)
                || noDefaultValue.contains(document.getText(range))){
            LOGGER.info("insertSetter replaceString withDefaultValue:{},offset:{},length:{}"
                    ,withDefaultValue, offset,noDefaultValue.length());
            DocumentUtil.replaceString(project,document, withDefaultValue,offset, noDefaultValue.length() );
            return withDefaultValue;
        }
        LOGGER.info("insertSetter  insertString noDefaultValue:{},offset:{}", noDefaultValue, offset);
        DocumentUtil.insertString(document,noDefaultValue,offset, project);
        return noDefaultValue;
    }

    private Integer getOffset(PojoLine pojo, String path) {
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


    private String genInsertLine(PsiElement data, PojoLine pojoLine, Boolean addDefaultValue) {
        List<String> retList = Lists.newArrayList();
        PsiMethod[] allMethods = null;
        if(data instanceof ClsClassImpl){
            ClsClassImpl clazz = (ClsClassImpl) data;
            allMethods = clazz.getAllMethods();
        }else if(data instanceof PsiClassImpl){
            PsiClassImpl clazz = (PsiClassImpl) data;
            allMethods = clazz.getAllMethods();
        }
        String linePrefix = pojoLine.getPreWhiteSpace() + pojoLine.getVariableName() + ".";
        if (allMethods != null && allMethods.length > 0) {
            for (PsiMethod method : allMethods) {
                if (method.getText().contains(" static ")) {
                    continue;
                }
                String lineSuffix = "();";
                if(addDefaultValue){
                    lineSuffix = "(" + getDefaultValue(method) + ");";
                }
                if(method.getText().contains(" set")){
                    retList.add(linePrefix + method.getName() + lineSuffix);
                }
            }
        }
        String line = StringUtils.EMPTY;
        for (String s : retList) {
            line += s;
            line +=  "\n";
        }
        LOGGER.info("insert line :{}", line);
        return line;
    }

    private static String getDefaultValue(PsiMethod returnType) {
        if(returnType == null){
            return StringUtils.EMPTY;
        }
        PsiParameter[] parameters = returnType.getParameterList().getParameters();
        if(parameters.length == 0){
            return StringUtils.EMPTY;
        }
        PsiParameter psiParameter =  parameters[0];
        String text = psiParameter.getType().getPresentableText();
        LOGGER.info("getDefault value returnType text:{}",text);
//         text = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(text).get(0);
        text = text.replace("(","");
        text = StringUtils.deleteWhitespace(text);
        if(text.startsWith("List<")){
            text = text.replace("<","s.<");
            return text + "newArrayList()";
        }
        if(text.startsWith("Map<")){
            text = text.replace("<","s.<");
            return text + "newHashMap()";
        }

        if(StringUtils.equalsIgnoreCase(text, "string")){
            return "StringUtils.EMPTY";
        }
        if(StringUtils.equalsIgnoreCase(text, "int")
                || StringUtils.equalsIgnoreCase(text, "Integer")){
            return "-1";
        }

        if(StringUtils.equalsIgnoreCase(text, "long")
                || StringUtils.equalsIgnoreCase(text, "Long")){
            return "-1L";
        }
        if(StringUtils.equalsIgnoreCase(text, "Boolean")
                || StringUtils.equalsIgnoreCase(text, "boolean")){
            return "false";
        }

        if(StringUtils.equalsIgnoreCase(text, "double")
                || StringUtils.equalsIgnoreCase(text, "Double")){
            return "-1d";
        }
        if(StringUtils.equalsIgnoreCase(text, "float")
                || StringUtils.equalsIgnoreCase(text, "Float")){
            return "-1.0f";
        }
        if(StringUtils.equalsIgnoreCase(text, "short")
                || StringUtils.equalsIgnoreCase(text, "Short")){
            return "-1";
        }

        if(StringUtils.equalsIgnoreCase(text, "BigDecimal")){
            return "new BigDecimal(-1)";
        }
        return "null";

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
        if (splits.get(0).equals(splits.get(4))
                && splits.get(2).equals("=")
                && splits.get(3).equals("new")
                && splits.get(5).equals("(")
                && splits.get(6).equals(")")
                && splits.get(7).equals(";")
                ) {
            PojoLine pojoLine = new PojoLine();
            pojoLine.setClassName(splits.get(0));
            pojoLine.setVariableName(splits.get(1));
            pojoLine.setLineNumber(lineNumber);
            pojoLine.setRawLine(line.toString());
            pojoLine.setLineStartPos(line.indexOf(pojoLine.getClassName()));
            pojoLine.setPreWhiteSpace(line.substring(0, pojoLine.getLineStartPos()));
            return pojoLine;
        }
        return null;
    }

    public static void main(String[] args) {

        PojoLine pojoLine = new PojoLine();
        pojoLine.setRawLine(StringUtils.EMPTY);
        pojoLine.setLineStartPos(-1);
        pojoLine.setClassName(StringUtils.EMPTY);
        pojoLine.setVariableName(StringUtils.EMPTY);
        pojoLine.setLineNumber(-1);
        pojoLine.setPreWhiteSpace(StringUtils.EMPTY);



    }
}

