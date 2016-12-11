package com.ccnode.codegenerator.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bruce.ge on 2016/12/11.
 */
public class MethodExistDialog extends DialogWrapper {

    private final Project myProject;

    private final String existMethodValue;

    private boolean override = false;

    public boolean isOverride() {
        return override;
    }

    public MethodExistDialog(@Nullable Project project, String existMethodValue) {
        super(project, true);
        myProject = project;
        this.existMethodValue = existMethodValue;
        init();
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel jpanel = new JPanel(new GridBagLayout());
        GridBagConstraints bag = new GridBagConstraints();
        bag.fill = GridBagConstraints.HORIZONTAL;
        bag.anchor = GridBagConstraints.WEST;
        bag.gridx = 0;
        bag.gridy = 0;
        jpanel.add(new JLabel("the method already exist in xml,the existed value is:"), bag);
        bag.gridx = 0;
        bag.gridy = 1;
        JTextArea comp = new JTextArea(existMethodValue);
        comp.setEditable(false);
        jpanel.add(comp, bag);

        bag.fill = GridBagConstraints.HORIZONTAL;
        bag.gridy = 2;
        jpanel.add(new JLabel("do you want to override them?"), bag);
        return jpanel;
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
        override = true;
    }
}
