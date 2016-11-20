package com.ccnode.codegenerator.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yepei.ye on 2015/11/27.
 * Description:
 */
public class JSONUtil {
    private static final Logger logger = LoggerWrapper.getLogger(JSONUtil.class);
    public static final ObjectMapper mapper = getMapper();

    public static final DateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String toJSONString(Object o) {
        return toJSONString(o,defaultFormat);
    }
    public static String toJSONString(Object o, DateFormat format) {
        String s= "{}";
        try {
            if (format != null) {
                mapper.setDateFormat(format);
            }
            s = mapper.writeValueAsString(o);
        } catch (Throwable e) {
            logger.error("JSON反序列化失败.",e);
            throw new RuntimeException(e);
        }
        return s;
    }

    @Nullable
    public static <T> T parseObject(String json, Class<T> cls) {
        try{
            if(StringUtils.isBlank(json)){
                return null;
            }
            return getMapper().readValue(json, cls);
        }catch(Exception e){
            logger.error("JSON反序列化异常",e);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String json, final Class<T> cls) {
        try {
            JavaType javaType = getCollectionType(ArrayList.class, cls);
            return (List<T>) mapper.readValue(json, javaType);
        } catch (Throwable e) {
            logger.error("JSON反序列化失败.", e);
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static <T> Set<T> parseSet(String json, final Class<T> cls) {
        try {
            JavaType javaType = getCollectionType(Set.class, cls);
            return (Set<T>) mapper.readValue(json, javaType);
        } catch (Throwable e) {
            logger.error("JSON反序列化失败.", e);
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static <K, V> Map<K, V> parseMap(String json, final Class<K> k_cls, final Class<V> v_cls) {
        try {
            JavaType javaType = getCollectionType(Map.class, k_cls, v_cls);
            return (Map<K, V>) mapper.readValue(json, javaType);
        } catch (IOException e) {
            logger.error("JSON反序列化失败.", e);
        }
        return null;
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setLocale(new Locale("zh", "CN"));
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.setDateFormat(defaultFormat);
        SerializerProvider provider = mapper.getSerializerProvider();
        try {
            provider.setNullValueSerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
                        throws IOException {
                    if (value == null) {
                        jgen.writeString("");
                    }
                }
            });
        } catch (Exception e) {
            logger.error("getMapper error", e);
        }
        return mapper;
    }
}
