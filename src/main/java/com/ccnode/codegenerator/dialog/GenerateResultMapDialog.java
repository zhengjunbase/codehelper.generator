package com.ccnode.codegenerator.dialog;

import com.ccnode.codegenerator.constants.MapperConstants;
import com.ccnode.codegenerator.pojo.FieldToColumnRelation;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Created by bruce.ge on 2016/12/20.
 */
public class GenerateResultMapDialog extends DialogWrapper {
    private List<String> props;

    private List<PropAndField> jTextFields;

    private String className;

    private JTextField columMapText;

    private FieldToColumnRelation relation;

    public GenerateResultMapDialog(Project project, List<String> prop, String className) {
        super(project, true);
        this.props = prop;
        this.className = className;
        this.jTextFields = new ArrayList<>();
        setTitle("generate the resultMap for type:" + className);
        initRelation();
        init();
    }

    public FieldToColumnRelation getRelation() {
        return relation;
    }

    public void setRelation(FieldToColumnRelation relation) {
        this.relation = relation;
    }

    @Override
    protected void doOKAction() {
        Map<String, String> fieldToColumnMap = new LinkedHashMap<>();
        if (StringUtils.isBlank(columMapText.getText())) {
            Messages.showErrorDialog("resultMapId is empety, please write things to it", "resultMapId is empty");
            return;
        }
        for (PropAndField field : jTextFields) {
            if (StringUtils.isNotBlank(field.getField().getText())) {
                fieldToColumnMap.put(field.getProp(), field.getField().getText().trim());
            } else {
                Messages.showErrorDialog("please write text to " + field.getField().getText(), "column is empty");
                return;
            }
        }
        relation.setResultMapId(columMapText.getText());
        relation.setFiledToColumnMap(fieldToColumnMap);
        super.doOKAction();
    }

    private void initRelation() {
        relation = new FieldToColumnRelation();
        Map<String, String> fieldToMap = new HashMap<>();
        for (String prop : props) {
            fieldToMap.put(prop, prop);
        }
        relation.setFiledToColumnMap(fieldToMap);
        relation.setResultMapId(MapperConstants.ALL_COLUMN_MAP);
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
        bag.insets = new Insets(0, 0, 5, 0);
        bag.gridwidth = 5;
        JLabel comp = new JLabel("can't find right resultMap for type:" + className + " let generate new resultMap to it");
        comp.setForeground(Color.RED);
        comp.setOpaque(true);
        jpanel.add(comp, bag);

        bag.gridwidth = 1;
        bag.gridy = 1;
        ButtonGroup group = new ButtonGroup();

        JRadioButton rawRadio = new JRadioButton("raw", true);

        jpanel.add(rawRadio, bag);
        group.add(rawRadio);

        bag.gridx = 2;
        JRadioButton snakeRadio = new JRadioButton("snake");
        jpanel.add(snakeRadio, bag);
        group.add(snakeRadio);


        bag.gridx = 3;

        JRadioButton lowerRadio = new JRadioButton("lowercase");
        jpanel.add(lowerRadio, bag);
        group.add(lowerRadio);

        bag.gridy++;
        bag.gridx = 0;
        jpanel.add(new JLabel("resultMapId"), bag);
        bag.gridx = 1;
        columMapText = new JTextField(MapperConstants.ALL_COLUMN_MAP);
        jpanel.add(columMapText, bag);

        bag.gridy++;
        bag.gridx = 0;
        jpanel.add(new JLabel("property"), bag);
        bag.gridx = 1;
        jpanel.add(new JLabel("column"), bag);
        for (String prop : this.props) {
            bag.gridy++;
            bag.gridx = 0;
            JLabel jLabel = new JLabel(prop);
            jpanel.add(jLabel, bag);
            JTextField field = new JTextField(prop);
            PropAndField e = new PropAndField();
            e.setProp(prop);
            e.setField(field);
            this.jTextFields.add(e);
            bag.gridx = 1;
            jpanel.add(field, bag);
        }

        rawRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (PropAndField field : jTextFields) {
                    field.getField().setText(field.getProp());
                }
            }
        });

        snakeRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (PropAndField field : jTextFields) {
                    field.getField().setText(GenCodeUtil.getUnderScore(field.getProp()));
                }
            }
        });

        lowerRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (PropAndField field : jTextFields) {
                    field.getField().setText(field.getProp().toLowerCase());
                }
            }
        });
        return jpanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
