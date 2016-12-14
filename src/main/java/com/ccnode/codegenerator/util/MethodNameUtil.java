package com.ccnode.codegenerator.util;

/**
 * Created by bruce.ge on 2016/12/14.
 */
public class MethodNameUtil {

    public static boolean checkValidTextStarter(String text) {
        return text.startsWith("find") || text.startsWith("update") || text.startsWith("delete") || text.startsWith("count");
    }
}
