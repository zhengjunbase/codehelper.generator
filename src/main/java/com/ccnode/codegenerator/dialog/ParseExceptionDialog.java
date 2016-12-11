package com.ccnode.codegenerator.dialog;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by bruce.ge on 2016/12/11.
 */
public class ParseExceptionDialog extends DialogWrapper {
    private String methodName;

    private Integer start;

    private Integer end;

    private String errorMsg;

    public ParseExceptionDialog(@Nullable Project project, String methodName, Integer start, Integer end, String errorMsg) {
        super(project, true);
        this.methodName = methodName;
        this.start = start;
        this.end = end;
        this.errorMsg = errorMsg;
        setTitle("parse methodname catch exception");
        setOKActionEnabled(false);
        init();
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel jPanel = new JPanel(new GridBagLayout());
        GridBagConstraints bag = new GridBagConstraints();
        bag.fill = GridBagConstraints.HORIZONTAL;
        bag.anchor = GridBagConstraints.WEST;
        bag.gridx = 0;
        bag.gridy = 0;
        bag.insets = new Insets(0, 0, 5, 0);
        if (start != null && end != null) {
            jPanel.add(new JLabel("methodname: "+methodName.substring(0, start)), bag);
            bag.gridx = 1;
            JLabel errorPart = new JLabel(methodName.substring(start, end));
            errorPart.setForeground(Color.RED);
            errorPart.setOpaque(true);
            jPanel.add(errorPart,bag);
            bag.gridx = 2;
            jPanel.add(new JLabel(methodName.substring(end)),bag);
        } else {
            jPanel.add(new JLabel("methodname: "+methodName), bag);
        }

        bag.gridx = 0;
        bag.gridy = 1;

        jPanel.add(new JLabel(errorMsg),bag);
        return jPanel;
    }
}
