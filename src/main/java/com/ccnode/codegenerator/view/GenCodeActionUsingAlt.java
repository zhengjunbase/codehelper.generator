package com.ccnode.codegenerator.view;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;

/**
 * Created by bruce.ge on 2016/12/9.
 */
public class GenCodeActionUsingAlt extends BaseGenerateAction {

    // If you register the action from Java code, this constructor is used to set the menu item name
    // (optionally, you can specify the menu description and an icon to display next to the menu item).
    // You can omit this constructor when registering the action in the plugin.xml file.
    public GenCodeActionUsingAlt() {
        // Set the menu item name.
        super(new GenCodeUsingAltHandler());
        // Set the menu item name, description and icon.
        // super("Text _Boxes","Item description",IconLoader.getIcon("/Mypackage/icon.png"));
    }
}
