package com.ccnode.codegenerator.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/29 16:50
 */
public class RegexUtil {

    public static String getMatch(@NotNull String pattern, @NotNull String line) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(line);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return StringUtils.EMPTY;
    }
}
