package com.ccnode.codegenerator.jpaparse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by bruce.ge on 2016/12/4.
 */
public class PatternUtils {
    private static Map<String, Pattern> patternMap = new HashMap<String, Pattern>();

    public static Pattern getPattern(String value) {
        Pattern s = patternMap.get(value);
        if (s == null) {
            Pattern ss = Pattern.compile(value);
            patternMap.put(value, ss);
            return ss;
        } else {
            return s;
        }
    }
}
