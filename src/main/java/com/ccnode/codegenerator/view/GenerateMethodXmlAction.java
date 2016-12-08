package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.jpaparse.QueryParser;
import com.ccnode.codegenerator.jpaparse.ReturnClassInfo;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.intellij.codeInsight.CodeInsightUtil;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.components.ServiceManager;
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
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
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

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        Module srcModule = ModuleUtilCore.findModuleForPsiElement(element);
        PsiClass srcClass = getContainingClass(element);

        if (srcClass == null) return;
        PsiDirectory srcDir = element.getContainingFile().getContainingDirectory();
        PsiPackage srcPackage = JavaDirectoryService.getInstance().getPackage(srcDir);
        PsiElement parent = element.getParent();
        if (!(parent instanceof PsiMethod)) {
            return;
        }
        PsiMethod method = (PsiMethod) parent;
        String methodName = method.getName();
        String returnClassName = method.getReturnType().getCanonicalText();
        if (returnClassName.startsWith(JAVALIST)) {
            returnClassName = returnClassName.substring(JAVALIST.length() + 1, returnClassName.length() - 1);
        }
        PsiClass pojoClass = null;
        PsiMethod addMethod = null;
        PsiMethod[] methods = srcClass.getMethods();
        for (PsiMethod classMethod : methods) {
            String name = classMethod.getName().toLowerCase();
            if (name.equals("insert") || name.equals("save") || name.equals("add")) {
                addMethod = classMethod;
                break;
            }
        }
        if (addMethod != null) {
            PsiParameterList parameterList = addMethod.getParameterList();
            PsiParameter[] parameters = parameterList.getParameters();
            if (parameters.length == 1) {
                PsiType type = parameters[0].getType();
                pojoClass = PsiTypesUtil.getPsiClass(type);
                //try to get it from the class.
            }
        }
        String srcClassName = srcClass.getName();
        if (pojoClass == null) {
            if (srcClassName.endsWith("Dao")) {
                String className = srcClassName.substring(0, srcClassName.length() - "Dao".length());
                PsiClass[] classesByName
                        = PsiShortNamesCache.getInstance(project).getClassesByName(className, GlobalSearchScope.moduleScope(srcModule));
                if (classesByName.length == 1) {
                    pojoClass = classesByName[0];
                } else {
                    //todo say there are two class with same name. let use choose with one.
                }
            } else {
                //todo show with error can't from the pojo class to inject.
            }
            //then get the file of xml get table name from it cause it the most right.
        }
        if (pojoClass == null) {
            //todo say can't find with pojo class file.
            return;
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
        } else {
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
            }, GlobalSearchScope.moduleScope(ModuleUtilCore.findModuleForPsiElement(method)));
            if (xmlFiles.size() == 0) {
                //todo no corresponding xml exist. go to tell the user.
                return;
            } else {
                psixml = xmlFiles.get(0);
            }
        }
        //extract field from pojoClass.
        PsiField[] allFields = pojoClass.getAllFields();
        List<String> props = extractProps(allFields);
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

        String existValues
                = methodAlreadyExist(psixml, methodName);

        //go get the props
        //create resultMap for return class.

        ReturnClassInfo info = buildReturnClassInfo(returnClassName, pojoClass);
        XmlTag sql = QueryParser.parse(rootTag, methodName, props, tableName, info);
        //todo display a form for user to choose from. let user check and edit.

        rootTag.addSubTag(sql, false);
//        Assert.assertEquals(a + 1, rootTag.getSubTags().length);
        CodeInsightUtil.positionCursor(project, psixml, rootTag.getSubTags()[rootTag.getSubTags().length - 1].getNextSibling());
        //navigate to the xml file.
        //add some thing with sql to deal with it.
        //make it cool to know.


        // add the sql to the xml file.


        //after extract the table name than generate xml file.


        //use this to find the class.
        //then get the returnclassName, go the load with the name.
        //find the Po class and get it's property.

//        PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
//        HashSet<VirtualFile> testFolder = new HashSet<>();
//        checkForTestRoots(srcModule, testFolder);
//        if (testFolder.isEmpty() && !propertiesComponent.getBoolean(CREATE_TEST_IN_THE_SAME_ROOT, false)) {
//            if (Messages.showOkCancelDialog(project, "Create test in the same source root?", "No Test Roots Found",
//                    Messages.getQuestionIcon()) != Messages.OK) {
//                return;
//            }
//            propertiesComponent.setValue(CREATE_TEST_IN_THE_SAME_ROOT, String.valueOf(true));
//        }
//        CreateTestDialog d = createTestDialog(project, srcModule, srcClass, srcPackage);
//        if (!d.showAndGet()) {
//            return;
//        }
//        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
//            @Override
//            public void run() {
//                TestFramework framework = d.getSelectedTestFrameworkDescriptor();
//                TestGenerator testGenerator = TestGenerators.INSTANCE.forLanguage(framework.getLanguage());
//                testGenerator.generateTest(project, d);
//            }
//        }, GENERATE_DAOXML, this);
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

    private static String methodAlreadyExist(PsiFile psixml, String methodName) {
        XmlTag rootTag = ((XmlFileImpl) psixml).getRootTag();
        XmlTag[] subTags = rootTag.getSubTags();
        Set<String> existIds = new HashSet<String>();
        for (XmlTag subTag : subTags) {
            XmlAttribute id = subTag.getAttribute("id");
            if (id != null && id.getValue() != null && id.getValue().equalsIgnoreCase(methodName)) {
                return subTag.getText();
            }
        }

        return null;

    }

    private List<String> extractProps(PsiField[] allFields) {
        List<String> props = new ArrayList<String>();
        for (PsiField psiField : allFields) {
            if (psiField.hasModifierProperty("private") && !psiField.hasModifierProperty("static")) {
                props.add(psiField.getName());
            }
        }
        return props;
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
        if (c == ' ' || c == '\t' || c == '\n') {
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
        PsiClass containingClass = getContainingClass(element);
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
            if (methodName.startsWith("find") || methodName.startsWith("update") || methodName.startsWith("delete")) {
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

        PsiClass containingClass = getContainingClass(psiElement);
        if (containingClass == null) return false;
        Module srcMoudle = ModuleUtilCore.findModuleForPsiElement(containingClass);
        if (srcMoudle == null) return false;
        if (containingClass.isAnnotationType() || containingClass instanceof PsiAnonymousClass || !containingClass.isInterface()) {
            return false;
        }
        return true;
    }


    private static PsiClass getContainingClass(PsiElement psiElement) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(psiElement, PsiClass.class, false);
        if (psiClass == null) {
            PsiFile containingFile = psiElement.getContainingFile();
            if (containingFile instanceof PsiClassOwner) {
                PsiClass[] classes = ((PsiClassOwner) containingFile).getClasses();
                if (classes.length == 1) {
                    return classes[0];
                }
            }
        }
        return psiClass;
    }

    @Nls
    @NotNull
    @Override
    public String getFamilyName() {
        return GENERATE_DAOXML;
    }
}
