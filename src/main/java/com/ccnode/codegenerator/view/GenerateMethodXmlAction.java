package com.ccnode.codegenerator.view;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiTypesUtil;
import com.intellij.testIntegration.createTest.CreateTestDialog;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.HashSet;

/**
 * Created by bruce.ge on 2016/12/5.
 */
public class GenerateMethodXmlAction extends PsiElementBaseIntentionAction {

    public static final String JAVALIST = "java.util.List";
    private static final String CREATE_TEST_IN_THE_SAME_ROOT = "create.test.in.the.same.root";
    public static final String GENERATE_DAOXML = "generate daoxml";

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
            //say can't find with pojo class file.
            return;
        }




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
