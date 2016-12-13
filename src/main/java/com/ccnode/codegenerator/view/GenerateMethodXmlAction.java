package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.dialog.MethodExistDialog;
import com.ccnode.codegenerator.jpaparse.ReturnClassInfo;
import com.ccnode.codegenerator.nextgenerationparser.QueryParseDto;
import com.ccnode.codegenerator.nextgenerationparser.QueryParser;
import com.ccnode.codegenerator.nextgenerationparser.buidler.ParamInfo;
import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryInfo;
import com.ccnode.codegenerator.nextgenerationparser.tag.XmlTagAndInfo;
import com.ccnode.codegenerator.pojo.MethodXmlPsiInfo;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.ccnode.codegenerator.util.PsiElementUtil;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.xml.XmlFileImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiNonJavaFileReferenceProcessor;
import com.intellij.psi.search.PsiSearchHelper;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.testIntegration.createTest.CreateTestDialog;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bruce.ge on 2016/12/5.
 */
public class GenerateMethodXmlAction extends PsiElementBaseIntentionAction {

    public static final String JAVALIST = "java.util.List";
    public static final String GENERATE_DAOXML = "generate daoxml";
    public static final String INSERT_INTO = "insert into";
    public static final String DAOCLASSEND = "Dao";

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        Module srcModule = ModuleUtilCore.findModuleForPsiElement(element);
        PsiClass srcClass = PsiElementUtil.getContainingClass(element);
        if (srcClass == null) return;
        //go to check if the pojo class exist.
        PsiClass pojoClass = PsiClassUtil.getPojoClass(srcClass);
        String srcClassName = srcClass.getName();
        if (pojoClass == null) {
            if (srcClassName.endsWith(DAOCLASSEND)) {
                String className = srcClassName.substring(0, srcClassName.length() - DAOCLASSEND.length());
                PsiClass[] classesByName
                        = PsiShortNamesCache.getInstance(project).getClassesByName(className, GlobalSearchScope.moduleScope(srcModule));
                if (classesByName.length == 1) {
                    pojoClass = classesByName[0];
                } else {
                    //todo say there are two class with same name. let user choose with one.
                }
            } else {
                //todo show with error can't from the pojo class to inject.
            }
            //then get the file of xml get table name from it cause it the most right.
        }
        //todo maybe wo can provide other method to know the real pojo class like annotation.
        if (pojoClass == null) {
            //todo say can't find with pojo class file.
            return;
        }

        PsiDirectory srcDir = element.getContainingFile().getContainingDirectory();
        PsiPackage srcPackage = JavaDirectoryService.getInstance().getPackage(srcDir);
        PsiElement parent = element.getParent();

        MethodXmlPsiInfo methodInfo = new MethodXmlPsiInfo();
        methodInfo.setPojoClass(pojoClass);
        if (parent instanceof PsiMethod) {
            setMethodValue((PsiMethod) parent, methodInfo);
        } else if (parent instanceof PsiJavaCodeReferenceElement) {
            String text = parent.getText();
            methodInfo.setMethodName(text);
//
//            return;
        }


        //when pojoClass is not null, then try to extract all property from it. then get the sql generated.do thing with batch.

        String xmlFileName = srcClassName + ".xml";
        String tableName = null;
        XmlFile psixml = null;
        PsiFile[] filesByName = PsiShortNamesCache.getInstance(project).getFilesByName(xmlFileName);
        if (filesByName.length > 0) {
            for (PsiFile file : filesByName) {
                if (file instanceof XmlFileImpl) {
                    XmlFileImpl xmlFile = (XmlFileImpl) file;
                    XmlTag rootTag = xmlFile.getRootTag();
                    String namespace = rootTag.getAttribute("namespace").getValue();
                    //only the name space is equal than deal with it.
                    if (namespace != null && namespace.equals(srcClass.getQualifiedName())) {
                        psixml = xmlFile;
                        break;
                    }
                }
            }
        }
        if (psixml == null) {
            //cant' find the file by name. then go to search it.
            PsiSearchHelper searchService = ServiceManager.getService(project, PsiSearchHelper.class);
            List<XmlFile> xmlFiles = new ArrayList<XmlFile>();
            searchService.processUsagesInNonJavaFiles("mapper", new PsiNonJavaFileReferenceProcessor() {
                @Override
                public boolean process(PsiFile file, int startOffset, int endOffset) {
                    if (file instanceof XmlFile) {
                        XmlFile xmlFile = (XmlFile) file;
                        if (xmlFile.getRootTag() != null) {
                            XmlAttribute namespace = xmlFile.getRootTag().getAttribute("namespace");
                            if (namespace != null && namespace.getValue().equals(srcClass.getQualifiedName())) {
                                xmlFiles.add(xmlFile);
                                return false;
                            }
                        }
                    }
                    return true;
                }
            }, GlobalSearchScope.moduleScope(ModuleUtilCore.findModuleForPsiElement(element)));
            if (xmlFiles.size() == 0) {
                //todo no corresponding xml exist. go to tell the user.
                return;
            } else {
                psixml = xmlFiles.get(0);
            }
        }
        //extract field from pojoClass.
        List<String> props = PsiClassUtil.extractProps(pojoClass);
        //find the corresponding xml file.
        XmlTag rootTag = psixml.getRootTag();
        XmlTag[] subTags = rootTag.getSubTags();

        boolean allColumMapExist = false;
        boolean allColumns = false;

        //boolean isReturnclassCurrentClass.
        for (XmlTag tag : subTags) {
            if (tag.getName().equalsIgnoreCase("insert")) {
                String insertText = tag.getValue().getText();
                //go format it.
                tableName = extractTable(insertText);
                if (tableName != null) {
                    break;
                }
            } else if (tag.getName().equalsIgnoreCase("resultMap")) {
                XmlAttribute id = tag.getAttribute("id");
                if (id != null && id.getValue().equals(MapperConstants.ALL_COLUMN_MAP)) {
                    allColumMapExist = true;
                }
            } else if (tag.getName().equalsIgnoreCase("sql")) {
                XmlAttribute id = tag.getAttribute("id");
                if (id != null && id.getValue().equals(MapperConstants.ALL_COLUMN)) {
                    allColumns = true;
                }
            }
            //then go next shall be the same.
            //deal with it.
        }

        //if not exist then add it to the file.
        if (!allColumMapExist) {
            String pojoFullName = pojoClass.getQualifiedName();
            String allColumnText = buildAllCoumnMap(props);
            XmlTag resultMap = rootTag.createChildTag("resultMap", "", allColumnText, false);
            resultMap.setAttribute("id", MapperConstants.ALL_COLUMN_MAP);
            resultMap.setAttribute("type", pojoFullName);
            rootTag.addSubTag(resultMap, true);
        }

        if (!allColumns) {
            String allColumn = buildAllColumn(props);
            XmlTag sql = rootTag.createChildTag("sql", "", allColumn, false);
            sql.setAttribute("id", MapperConstants.ALL_COLUMN);
            rootTag.addSubTag(sql, true);
        }

        XmlTag existTag
                = methodAlreadyExist(psixml, methodInfo.getMethodName());

        if (existTag != null) {
            MethodExistDialog exist = new MethodExistDialog(project, existTag.getText());
            boolean b = exist.showAndGet();
            if (!b) {
                return;
            } else {
                existTag.delete();
            }
        }

        // TODO: 2016/12/12 if method contain return class, we need to choose the one with right return type.

//
//        ReturnClassInfo info = buildReturnClassInfo(methodInfo.getReturnClassName(), pojoClass);
//        XmlTag sql = null;
//        try {
//            sql = QueryParser.parse(rootTag, methodInfo.getMethodName(), props, tableName, info);
//        } catch (ParseException e) {
//            if (e.getTerm() != null) {
//                ParseExceptionDialog d = new ParseExceptionDialog(project, methodInfo.getMethodName(), e.getTerm().getStart(), e.getTerm().getEnd(), e.getMessage());
//                d.showAndGet();
//            } else {
//                ParseExceptionDialog d = new ParseExceptionDialog(project, methodInfo.getMethodName(), null, null, e.getMessage());
//                d.showAndGet();
//            }
//            return;
//        }

//        rootTag.addSubTag(sql, false);
        methodInfo.setTableName(tableName);
        QueryParseDto parseDto = QueryParser.parse(props, methodInfo);
        XmlTagAndInfo choosed = null;
        if (parseDto.getHasMatched()) {
            //dothings in it.
            List<QueryInfo> queryInfos = parseDto.getQueryInfos();
            //generate tag for them
            List<XmlTagAndInfo> tags = new ArrayList<>();
            for (QueryInfo info : queryInfos) {
                XmlTagAndInfo tag = generateTag(rootTag, info, methodInfo.getMethodName());
                tags.add(tag);
            }

            if (tags.size() > 1) {
                //let user choose with one.
            } else {
                choosed = tags.get(0);
            }
        }
        if (methodInfo.getMethod() == null) {
            //means we need to insert the text into it.
            String insertBefore = choosed.getInfo().getMethodReturnType() + " ";
            String insertNext = "(";
            for (int i = 0; i < choosed.getInfo().getParamInfos().size(); i++) {
                ParamInfo info = choosed.getInfo().getParamInfos().get(i);
                insertNext += "@Param(\"" + info.getParamAnno() + "\")" + info.getParamType() + " " + info.getParamValue();
                if (i != choosed.getInfo().getParamInfos().size() - 1) {
                    insertNext += ",";
                }
            }
            insertNext += ");";
            //insert text into it.
            Document document = PsiDocumentManager.getInstance(project).getDocument(srcClass.getContainingFile());
            document.insertString(element.getTextOffset(), insertBefore);

            document.insertString(element.getTextOffset() + element.getTextLength() + insertBefore.length(), insertNext);
        }

        rootTag.addSubTag(choosed.getXmlTag(), false);
        //let user choose with one.

        CodeInsightUtil.positionCursor(project, psixml, rootTag.getSubTags()[rootTag.getSubTags().length - 1].getNextSibling());
    }

    private XmlTagAndInfo generateTag(XmlTag rootTag, QueryInfo info, String methodName) {
        XmlTagAndInfo xmlTagAndInfo = new XmlTagAndInfo();
        xmlTagAndInfo.setInfo(info);
        XmlTag select = rootTag.createChildTag(info.getType(), "", info.getSql(), false);
        select.setAttribute("id", methodName);
        if (info.getReturnMap() != null) {
            select.setAttribute("resultMap", info.getReturnMap());
        } else if (info.getReturnClass() != null) {
            select.setAttribute("resultType", info.getReturnClass());
        }
        xmlTagAndInfo.setXmlTag(select);
        return xmlTagAndInfo;

    }

    private void setMethodValue(PsiMethod method, MethodXmlPsiInfo info) {
        String returnClassName = method.getReturnType().getCanonicalText();
        if (returnClassName.startsWith(JAVALIST)) {
            returnClassName = returnClassName.substring(JAVALIST.length() + 1, returnClassName.length() - 1);
        }
        info.setMethod(method);
        info.setMethodName(method.getName());
        info.setReturnClassName(returnClassName);
    }

    private ReturnClassInfo buildReturnClassInfo(String returnClassName, PsiClass pojoClass) {
        ReturnClassInfo info = new ReturnClassInfo();
        info.setReturnClassName(returnClassName);
        if (returnClassName.startsWith("java.lang")) {
            info.setBasicType(true);
        } else if (returnClassName.equals(pojoClass.getQualifiedName())) {
            info.setResultMap(MapperConstants.ALL_COLUMN_MAP);
        }
        return info;
    }

    private String buildAllColumn(List<String> props) {
        StringBuilder bu = new StringBuilder();
        for (int i = 0; i < props.size(); i++) {
            bu.append("\n\t").append(GenCodeUtil.getUnderScore(props.get(i)));
            if (i != props.size() - 1) {
                bu.append(",");
            }
        }
        bu.append("\n");
        return bu.toString();
    }

    private String buildAllCoumnMap(List<String> props) {
        StringBuilder builder = new StringBuilder();
        for (String prop : props) {
            builder.append("\n\t").append("<result column=\"").append(GenCodeUtil.getUnderScore(prop)).append("\"")
                    .append(" property=\"").append(prop).append("\"/>");
        }
        builder.append("\n");
        return builder.toString();
    }

    private static XmlTag methodAlreadyExist(PsiFile psixml, String methodName) {
        XmlTag rootTag = ((XmlFileImpl) psixml).getRootTag();
        XmlTag[] subTags = rootTag.getSubTags();
        Set<String> existIds = new HashSet<String>();
        for (XmlTag subTag : subTags) {
            XmlAttribute id = subTag.getAttribute("id");
            if (id != null && id.getValue() != null && id.getValue().equalsIgnoreCase(methodName)) {
                return subTag;
            }
        }

        return null;

    }

    private static String extractTable(String insertText) {
        if (insertText.length() == 0) {
            return null;
        }
        String formattedInsert = formatBlank(insertText).toLowerCase();
        int i = formattedInsert.indexOf(INSERT_INTO);
        if (i == -1) {
            return null;
        }
        int s = i + INSERT_INTO.length() + 1;
        StringBuilder resBuilder = new StringBuilder();
        for (int j = s; j < formattedInsert.length(); j++) {
            char c = formattedInsert.charAt(j);
            if (!isBlankChar(c)) {
                resBuilder.append(c);
            } else {
                break;
            }
        }
        if (resBuilder.length() > 0) {
            return resBuilder.toString();
        } else {
            return null;
        }
    }

    private static String formatBlank(String insertText) {
        StringBuilder result = new StringBuilder();
        char firstChar = insertText.charAt(0);
        result.append(firstChar);
        boolean before = isBlankChar(firstChar);
        for (int i = 1; i < insertText.length(); i++) {
            char curChar = insertText.charAt(i);
            boolean cur = isBlankChar(curChar);
            if (cur && before) {
                continue;
            } else {
                result.append(curChar);
                before = cur;
            }
        }
        return result.toString();
    }

    private static boolean isBlankChar(char c) {
        if (c == ' ' || c == '\t' || c == '\n' || c == '(' || c == '<' || c == ')' || c == '>') {
            return true;
        }
        return false;
    }

    private CreateTestDialog createTestDialog(Project project, Module srcModule, PsiClass srcClass, PsiPackage srcPackage) {
        return new CreateTestDialog(project, GENERATE_DAOXML, srcClass, srcPackage, srcModule);
    }

    private static void checkForTestRoots(Module srcModule, HashSet<VirtualFile> testFolder) {
        checkForTestRoots(srcModule, testFolder, new HashSet<Module>());
    }

    private static void checkForTestRoots(Module srcModule, HashSet<VirtualFile> testFolder, HashSet<Module> processed) {
        boolean isFirst = processed.isEmpty();
        if (!processed.add(srcModule)) return;
        testFolder.addAll(ModuleRootManager.getInstance(srcModule).getSourceRoots(JavaSourceRootType.TEST_SOURCE));
        if (isFirst && !testFolder.isEmpty()) return;
        HashSet<Module> modules = new HashSet<>();
        ModuleUtilCore.collectModulesDependsOn(srcModule, modules);
        for (Module module : modules) {
            checkForTestRoots(module, testFolder, processed);
        }
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        if (!isAvailbleForElement(element)) return false;
        PsiClass containingClass = PsiElementUtil.getContainingClass(element);
        assert containingClass != null;
        PsiElement leftBrace = containingClass.getLBrace();
        if (leftBrace == null) {
            return false;
        }
        if (element instanceof PsiMethod) {
            PsiMethod method = (PsiMethod) element;
        }

        PsiElement parent = element.getParent();
        if (parent instanceof PsiMethod) {
            // ok.
            PsiMethod method = (PsiMethod) parent;
            String methodName = method.getName().toLowerCase();
            if (methodName.startsWith("find") || methodName.startsWith("update") || methodName.startsWith("delete") || methodName.startsWith("count")) {
                return true;
            }
        }
        if (parent instanceof PsiJavaCodeReferenceElement) {
            PsiJavaCodeReferenceElement referenceElement = (PsiJavaCodeReferenceElement) parent;
            String text = referenceElement.getText().toLowerCase();
            if (text.startsWith("find") || text.startsWith("update") || text.startsWith("delete") || text.startsWith("count")) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    @Override
    public String getText() {
        return GENERATE_DAOXML;
    }

    public static boolean isAvailbleForElement(PsiElement psiElement) {
        if (psiElement == null) {
            return false;
        }

        PsiClass containingClass = PsiElementUtil.getContainingClass(psiElement);
        if (containingClass == null) return false;
        Module srcMoudle = ModuleUtilCore.findModuleForPsiElement(containingClass);
        if (srcMoudle == null) return false;
        if (containingClass.isAnnotationType() || containingClass instanceof PsiAnonymousClass || !containingClass.isInterface()) {
            return false;
        }
        return true;
    }


    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return GENERATE_DAOXML;
    }
}
