package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.dialog.ChooseXmlToUseDialog;
import com.ccnode.codegenerator.dialog.GenerateResultMapDialog;
import com.ccnode.codegenerator.dialog.MethodExistDialog;
import com.ccnode.codegenerator.jpaparse.ReturnClassInfo;
import com.ccnode.codegenerator.nextgenerationparser.QueryParseDto;
import com.ccnode.codegenerator.nextgenerationparser.QueryParser;
import com.ccnode.codegenerator.nextgenerationparser.buidler.ParamInfo;
import com.ccnode.codegenerator.nextgenerationparser.buidler.QueryInfo;
import com.ccnode.codegenerator.nextgenerationparser.tag.XmlTagAndInfo;
import com.ccnode.codegenerator.pojo.FieldToColumnRelation;
import com.ccnode.codegenerator.pojo.MethodXmlPsiInfo;
import com.ccnode.codegenerator.util.MethodNameUtil;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.ccnode.codegenerator.util.PsiDocumentUtils;
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
import com.intellij.openapi.ui.Messages;
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
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.*;

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
                    // TODO: 2016/12/14 does it need to find the pojo file with name?.
                }
            }
            //then get the file of xml get table name from it cause it the most right.
        }
        //todo maybe wo can provide other method to know the real pojo class like annotation.
        if (pojoClass == null) {
            Messages.showErrorDialog("please provide an insert method with corresponding database class as parameter in this class" +
                    "\n like 'insert(User user)'\n" +
                    "we need the 'User' class to parse your method", "can't find the class for the database table");
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
        } else if (element instanceof PsiWhiteSpace) {
            PsiElement lastMatchedElement = findLastMatchedElement(element);
            element = lastMatchedElement;
            String text = lastMatchedElement.getText();
            methodInfo.setMethodName(text);
        }


        //when pojoClass is not null, then try to extract all property from it. then get the sql generated.do thing with batch.

        String xmlFileName = srcClassName + ".xml";
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
            //cant' find the file by name. then go to search it. will only search the file once.
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
                Messages.showErrorDialog("can't find xml file for namespace " + srcClassName, "xml file not found error");
                return;
            } else if (xmlFiles.size() == 1) {
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
        String tableName = null;

        FieldToColumnRelation relation = null;
        boolean hasResultType = false;
        for (XmlTag tag : subTags) {
            if (tag.getName().equalsIgnoreCase("insert")) {
                String insertText = tag.getValue().getText();
                //go format it.
                tableName = extractTable(insertText);
                if (tableName != null) {
                    break;
                }
            } else if (relation == null && tag.getName().equalsIgnoreCase("resultMap")) {
                String resultMapId;
                XmlAttribute id = tag.getAttribute("id");
                if (id != null && id.getValue() != null) {
                    resultMapId = id.getValue();
                    XmlAttribute typeAttribute = tag.getAttribute("type");
                    if (typeAttribute != null && typeAttribute.getValue() != null && typeAttribute.getValue().trim().equals(pojoClass.getQualifiedName())) {
                        //mean we find the corresponding prop.
                        hasResultType = true;
                        relation = extractFieldAndColumnRelation(tag, props, resultMapId);
                    }
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

        if (StringUtils.isEmpty(tableName)) {
            Messages.showErrorDialog("can't find table name from your " + psixml.getName() + "" +
                    "\nplease add a correct insert method into the file\n" +
                    "like\n'<insert id=\"insert\">\n" +
                    "        INSERT INTO user ....\n</insert>\n" +
                    "so we can extract the table name 'user' from it", "can't extract table name");
            return;
        }

        //if not exist then add it to the file.
//        if (!allColumMapExist) {
//            String pojoFullName = pojoClass.getQualifiedName();
//            String allColumnText = buildAllCoumnMap(props);
//            XmlTag resultMap = rootTag.createChildTag("resultMap", "", allColumnText, false);
//            resultMap.setAttribute("id", MapperConstants.ALL_COLUMN_MAP);
//            resultMap.setAttribute("type", pojoFullName);
//            rootTag.addSubTag(resultMap, true);
//        }

        PsiDocumentManager psiDocumentManager = PsiDocumentManager.getInstance(project);
        if (relation == null) {
            if (hasResultType) {
                Messages.showErrorDialog("please check with your resultMap\n" +
                        "dose it contain all the property of " + pojoClass.getQualifiedName() + "? ", "proprety in resultMap is not complete");
                return;
            } else {
                GenerateResultMapDialog generateResultMapDialog = new GenerateResultMapDialog(project, props, pojoClass.getQualifiedName());
                boolean b = generateResultMapDialog.showAndGet();
                if (!b) {
                    return;
                }
//                Messages.showErrorDialog("please provide a resultMap the type is:" + pojoClass.getQualifiedName() + "\n" +
//                        "in xml path:" + psixml.getVirtualFile().getPath(), "can't find resultMap in your mapper xml");
                //create tag into the file.
                FieldToColumnRelation relation1 = generateResultMapDialog.getRelation();
                //use to generate resultMap
                String allColumnMap = buildAllCoumnMap(relation1.getFiledToColumnMap());
                XmlTag resultMap = rootTag.createChildTag("resultMap", "", allColumnMap, false);
                resultMap.setAttribute("id", relation1.getResultMapId());
                resultMap.setAttribute("type", pojoClass.getQualifiedName());
                rootTag.addSubTag(resultMap, true);
                Document xmlDocument = psiDocumentManager.getDocument(psixml);
                PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, xmlDocument);

                relation = convertToRelation(relation1);
            }
        }

        methodInfo.setRelation(relation);

        if (!allColumns) {
            String allColumn = buildAllColumn(relation.getFiledToColumnMap());
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
        rootTag = psixml.getRootTag();
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
                ChooseXmlToUseDialog chooseXmlToUseDialog = new ChooseXmlToUseDialog(project, tags);
                boolean b = chooseXmlToUseDialog.showAndGet();
                if (!b) {
                    return;
                } else {
                    choosed = tags.get(chooseXmlToUseDialog.getChoosedIndex());
                }

            } else {
                choosed = tags.get(0);
            }
        } else {
            //there is no match the current methodName display error msg for user.
            String content = "";
            List<String> errorMsg = parseDto.getErrorMsg();
            for (int i = 0; i < errorMsg.size(); i++) {
                content += errorMsg.get(i) + "\n";
            }
            Messages.showErrorDialog(content, "can't parse the methodName");
            return;
        }
        if (methodInfo.getMethod() == null) {
            //means we need to insert the text into it.
            String insertBefore = choosed.getInfo().getMethodReturnType() + " ";
            String insertNext = "(";
            if (choosed.getInfo().getParamInfos() != null) {
                for (int i = 0; i < choosed.getInfo().getParamInfos().size(); i++) {
                    ParamInfo info = choosed.getInfo().getParamInfos().get(i);
                    insertNext += "@Param(\"" + info.getParamAnno() + "\")" + info.getParamType() + " " + info.getParamValue();
                    if (i != choosed.getInfo().getParamInfos().size() - 1) {
                        insertNext += ",";
                    }
                }
            }
            insertNext += ");";
            //insert text into it.
            Document document = psiDocumentManager.getDocument(srcClass.getContainingFile());
            document.insertString(element.getTextOffset(), insertBefore);
            document.insertString(element.getTextOffset() + element.getTextLength() + insertBefore.length(), insertNext);
            PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, document);
        }

        psixml.getRootTag().addSubTag(choosed.getXmlTag(), false);

//        Document xmlDocument = psiDocumentManager.getDocument(psixml);
// // TODO: 2016/12/15 may be need add new line before the query.
//        XmlTag tag = rootTag.getSubTags()[rootTag.getSubTags().length - 1];
//        xmlDocument.insertString(tag.getTextOffset(), "\n\n<!--auto generated Code-->\n");
        //let user choose with one.


        Document xmlDocument = psiDocumentManager.getDocument(psixml);
        PsiDocumentUtils.commitAndSaveDocument(psiDocumentManager, xmlDocument);

        CodeInsightUtil.positionCursor(project, psixml, rootTag.getSubTags()[rootTag.getSubTags().length - 1]);
    }

    private FieldToColumnRelation convertToRelation(FieldToColumnRelation relation1) {
        FieldToColumnRelation relation = new FieldToColumnRelation();
        relation.setResultMapId(relation1.getResultMapId());
        Map<String, String> fieldToColumnLower = new LinkedHashMap<>();
        for (String prop : relation1.getFiledToColumnMap().keySet()) {
            fieldToColumnLower.put(prop.toLowerCase(), relation1.getFiledToColumnMap().get(prop));
        }
        relation.setFiledToColumnMap(fieldToColumnLower);
        return relation;
    }

    private FieldToColumnRelation extractFieldAndColumnRelation(XmlTag tag, List<String> props, String resultMapId) {
        Set<String> propSet = new HashSet<>(props);
        XmlTag[] subTags = tag.getSubTags();
        if (subTags == null || subTags.length == 0) {
            return null;
        }
        Map<String, String> fieldAndColumnMap = new LinkedHashMap<>();
        for (XmlTag propTag : subTags) {
            XmlAttribute column = propTag.getAttribute("column");
            XmlAttribute property = propTag.getAttribute("property");
            if (column == null || column.getValue() == null || property == null || property.getValue() == null) {
                continue;
            }
            String columnString = column.getValue().trim();
            String propertyString = property.getValue().trim();
            if (!propSet.contains(propertyString)) {
                continue;
            }
            fieldAndColumnMap.put(propertyString.toLowerCase(), columnString);
            propSet.remove(propertyString);
        }
        //mean there are not all property in the resultMap.
        if (propSet.size() != 0) {
            return null;
        }
        FieldToColumnRelation relation = new FieldToColumnRelation();
        relation.setFiledToColumnMap(fieldAndColumnMap);
        relation.setResultMapId(resultMapId);
        return relation;
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

    private String buildAllColumn(Map<String, String> filedToColumnMap) {
        StringBuilder bu = new StringBuilder();
        int i = 0;
        for (String s : filedToColumnMap.keySet()) {
            i++;
            bu.append("\n\t").append("`" + filedToColumnMap.get(s) + "`");
            if (i != filedToColumnMap.size()) {
                bu.append(",");
            }
        }
        bu.append("\n");
        return bu.toString();
    }

    private String buildAllCoumnMap(Map<String, String> fieldToColumnMap) {
        StringBuilder builder = new StringBuilder();
        for (String prop : fieldToColumnMap.keySet()) {
            builder.append("\n\t").append("<result column=\"").append(fieldToColumnMap.get(prop)).append("\"")
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
            // TODO: 2016/12/14 now we don't support to perform on method.
            return false;
        }

        PsiElement parent = element.getParent();
        if (parent instanceof PsiMethod) {
            // ok.
            return false;
//            PsiMethod method = (PsiMethod) parent;
//            String methodName = method.getName().toLowerCase();
//            if (methodName.startsWith("find") || methodName.startsWith("update") || methodName.startsWith("delete") || methodName.startsWith("count")) {
//                return true;
//            }
        }
        if (element instanceof PsiWhiteSpace) {
            PsiElement element1 = findLastMatchedElement(element);
            if (element1 == null) {
                return false;
            }
            return true;
        }
        if (parent instanceof PsiJavaCodeReferenceElement) {
            PsiJavaCodeReferenceElement referenceElement = (PsiJavaCodeReferenceElement) parent;
            String text = referenceElement.getText().toLowerCase();
            if (MethodNameUtil.checkValidTextStarter(text)) {
                return true;
            }
        }
        return false;
    }

    private PsiElement findLastMatchedElement(PsiElement element) {
        PsiElement prevSibling = element.getPrevSibling();
        while (prevSibling != null && isIgnoreText(prevSibling.getText())) {
            prevSibling = prevSibling.getPrevSibling();
        }
        if (prevSibling != null) {
            String lowerCase = prevSibling.getText().toLowerCase();
            if (MethodNameUtil.checkValidTextStarter(lowerCase)) {
                return prevSibling;
            }
        }
        return null;
    }

    private boolean isIgnoreText(String text) {
        return (text.equals("")) || (text.equals("\n")) || text.equals(" ");
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
