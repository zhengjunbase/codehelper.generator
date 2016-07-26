package com.ccnode.codegenerator.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * What always stop you is what you always believe.
 * <p/>
 * Created by zhengjun.du on 2016/01/09 19:22
 */
public class PojoUtil {

    private final static Logger LOGGER = LoggerWrapper.getLogger(PojoUtil.class);


    public static String avoidEmptyString(String s){
        if(StringUtils.isBlank(s)){
            return StringUtils.EMPTY;
        }
        return s;
    }
    public static <T> List<T> avoidEmptyList(List<T> oldList){
        if(oldList == null){
            return Lists.newArrayList();
        }
        return oldList;
    }

    /**
     * list去重,同时会保持list的原有顺序
     * @param oldList
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> List<T> removeDuplicateElement(List<T> oldList){
        Set<T> set = Sets.newHashSet();
        List<T> retList = Lists.newArrayList();
        for (T t : oldList) {
            if(set.add(t)){
                retList.add(t);
            }
        }
        return retList;
    }


    public static String avoidNull(String s){
        return s == null ? StringUtils.EMPTY : s;
    };

    private static Object getDefaultValue(Class clazz, String fieldName) {
        if (clazz.equals(String.class)) {
            return StringUtils.EMPTY;
        } else if (clazz.equals(Long.class)) {
            return -1L;
        } else if (clazz.equals(Float.class)) {
            return 0.0f;
        } else if (clazz.equals(Integer.class)) {
            return -1;
        } else if (clazz.equals(Short.class)) {
            return (short) 0;
        } else if (clazz.equals(BigDecimal.class)) {
            return new BigDecimal(0);
        } else if (clazz.equals(Date.class)) {
            if(fieldName.equalsIgnoreCase("createTime")){
                return new Date();
            }
            if(fieldName.equalsIgnoreCase("lastUpdate")){
                return null;
            }
            return DateUtil.convertYYYYMMDD("1001-01-01");
        }
        return null;
    }

}
