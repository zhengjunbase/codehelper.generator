package com.ccnode.codegenerator.util;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p/>
 * Created by zhengjun.du on 2015/11/19 17:34
 */
public class LogHelper {

    private static final Logger LOGGER = LoggerWrapper.getLogger(LogHelper.class);

    /**
     * 抽取出来一个toString的方法，以后打对象日志尽量用这个，
     * 如果我们以后要更换打对象日志的方式，修改这个方法就行。
     * @param o
     * @return
     */
    public static String toString(Object o){
        if(o == null){
            return StringUtils.EMPTY;
        }
        try {
            if(o instanceof Throwable){
                return Throwables.getStackTraceAsString((Throwable) o);
            }
            if(o.getClass().equals(String.class)){
                return (String)o;
            }
            return JSONUtil.toJSONString(o);
        } catch (Exception e) {
            LOGGER.error("LogHelper writeValueAsString exception {}", o, e);
            return o.toString();
        }
    }
    public static String[] toString(Object... objects){
        List<String> retList = Lists.newArrayList();
        for (Object o : objects) {
            if(o instanceof Throwable){
                retList.add(Throwables.getStackTraceAsString((Throwable) o));
            }else{
                retList.add(LogHelper.toString(o));

            }
        }
        String[] arr = new String[retList.size()];
        return retList.toArray(arr);
    }

}
