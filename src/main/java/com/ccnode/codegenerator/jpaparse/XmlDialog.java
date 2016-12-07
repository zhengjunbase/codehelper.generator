package com.ccnode.codegenerator.jpaparse;

import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiPackage;
import com.intellij.refactoring.ui.MemberSelectionTable;
import com.intellij.testIntegration.JavaTestFramework;
import com.intellij.testIntegration.TestFramework;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bruce.ge on 2016/12/7.
 */
public class XmlDialog extends DialogWrapper {

    private final Project myProject;

    private final String existedValue;

    private final PsiClass myTargetClass;

    private final Module myTargetModule;

    private PsiDirectory myTargetDirectory;

    private TestFramework mySelectedFramework;

    private final List<JRadioButton> myLibraryButtons = new ArrayList<JRadioButton>();

    private EditorTextField myTargetClassNameField;

    private ReferenceEditorComboWithBrowseButton mySuperClassField;

    private ReferenceEditorComboWithBrowseButton myTargetPackageFiled;

    private JCheckBox myGenerateBeforeBox;

    private JCheckBox myGenerateAfterBox;

    private MemberSelectionTable myMethodsTable;

    private JButton myFixLibraryButton;

    private JPanel myFixLibraryPanel;

    private JRadioButton myDefaultLibraryButton;

    protected XmlDialog(@Nullable Project project, String title, PsiClass targetClass, PsiPackage targetPackage, Module targetModule, String existValue) {
        super(project, true);
        myProject = project;
        myTargetClass = targetClass;
        myTargetModule = targetModule;
        existedValue = existValue;
        initControls(targetClass, targetPackage);
    }

    private void initControls(PsiClass targetClass, PsiPackage targetPackage) {
        ButtonGroup group = new ButtonGroup();
        Map<String, JRadioButton> nameToButtonMap = new HashMap<>();
        List<com.intellij.openapi.util.Pair<String, JRadioButton>> attachedLibraries = new ArrayList<>();
        for (final TestFramework descriptor : Extensions.getExtensions(TestFramework.EXTENSION_NAME)) {
            final JRadioButton b = new JRadioButton(descriptor.getName());
            if (descriptor instanceof JavaTestFramework) {
                b.setMnemonic(((JavaTestFramework) descriptor).getMnemonic());
            }
            myLibraryButtons.add(b);
            group.add(b);
            nameToButtonMap.put(descriptor.getName(), b);
            if (descriptor.isLibraryAttached(myTargetModule)) {
                attachedLibraries.add(com.intellij.openapi.util.Pair.create(descriptor.getName(), b));
            }
        }

    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return null;
    }
}
