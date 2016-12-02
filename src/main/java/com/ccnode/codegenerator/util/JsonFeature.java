package com.ccnode.codegenerator.util;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/30 22:25
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 用于标记可编辑的json特性
 *
 * @author miao.yang susing@gmail.com
 * @since 14-3-24.
 */
public enum JsonFeature {

    AUTO_CLOSE_TARGET(JsonGenerator.Feature.AUTO_CLOSE_TARGET, true),

    INDENT_OUTPUT(SerializationFeature.INDENT_OUTPUT, false),

    WRITE_DATES_AS_TIMESTAMPS(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true),
    WRITE_NULL_MAP_VALUES(SerializationFeature.WRITE_NULL_MAP_VALUES, false),

    INCLUSION_NOT_NULL(JsonInclude.Include.NON_NULL, false),
    INCLUSION_NOT_EMPTY(JsonInclude.Include.NON_EMPTY, false),

    FAIL_ON_EMPTY_BEANS(SerializationFeature.FAIL_ON_EMPTY_BEANS, false),
    FAIL_ON_UNKNOWN_PROPERTIES(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false),

    ALLOW_COMMENTS(JsonParser.Feature.ALLOW_COMMENTS, true),
    ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true),
    ALLOW_NON_NUMERIC_NUMBERS(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true),
    ALLOW_NUMERIC_LEADING_ZEROS(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true),
    ALLOW_UNQUOTED_CONTROL_CHARS(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true),
    ALLOW_UNQUOTED_FIELD_NAMES(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true),
    ALLOW_SINGLE_QUOTES(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    private static final long defaults;

    static {
        int flags = 0;
        for (JsonFeature f : values()) {
            if (f.enabledByDefault()) {
                flags |= f.getMask();
            }
        }
        defaults = flags;
    }

    private final Object feature;
    private final boolean enabledByDefault;
    private final int mask;


    private JsonFeature(Object feature, boolean enabledByDefault) {
        this.feature = feature;
        this.enabledByDefault = enabledByDefault;
        mask = 1 << ordinal();
    }

    public boolean enabledByDefault() {
        return enabledByDefault;
    }

    int getMask() {
        return mask;
    }

    Object getFeature() {
        return feature;
    }

    boolean isEnabled(long flags) {
        return (flags & mask) != 0;
    }

    long enable(long flags) {
        return flags | mask;
    }

    long disable(long flags){
        return flags & (~mask);
    }

    public static long defaults() {
        return defaults;
    }
}
