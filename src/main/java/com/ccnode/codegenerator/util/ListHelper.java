package com.ccnode.codegenerator.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang.math.RandomUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/06/15 17:37
 */
public class ListHelper {

    public static <T> List<T> avoidNullList(List<T> oldList) {
        if (oldList == null) {
            return Lists.newArrayList();
        }
        return oldList;
    }

    @Nullable
    public static <T> T getRandomElement(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(RandomUtils.nextInt(list.size()));
    }

    public static <T> boolean isNotEmpty(List<T> list) {
        return !isEmpty(list);
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> List<T> copyList(List<T> originList) {
        return new ArrayList<T>(originList);
    }

    @Nullable
    public static <T> T nullOrFirstElement(List<T> list){
        if(isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    @Nullable
    public static <T> T nullOrLastElement(List<T> list){
        if(isEmpty(list)){
            return null;
        }
        return list.get(list.size() - 1);
    }

}
