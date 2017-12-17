package com.ccnode.codegenerator.util;

import javax.swing.*;

public class IconUtils {

    private static ImageIcon methodIcon = new ImageIcon(IconUtils.class.getClassLoader().getResource("icon/down.png"));

    private static ImageIcon xmlIcon = new ImageIcon(IconUtils.class.getClassLoader().getResource("icon/up.png"));

    public static Icon useMyBatisIcon() {
        return methodIcon;
    }

    public static Icon useXmlIcon() {
        return xmlIcon;
    }
}
