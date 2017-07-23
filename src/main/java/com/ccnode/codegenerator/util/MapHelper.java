package com.ccnode.codegenerator.util;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2017/07/23 13:09
 */
public class MapHelper {

    @Nullable
    public static <K, V> V getFirstMatch(Map<K, V> map, K... keys) {
        if (map == null || keys.length == 0) {
            return null;
        }
        for (K each : keys) {
            V v = map.get(each);
            if (v != null) {
                return v;
            }
        }
        return null;
    }
}
