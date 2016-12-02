package com.ccnode.codegenerator.util;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/30 22:24
 */

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.ExecutionException;

/**
 * @author miao.yang susing@gmail.com
 * @since 14-3-24.
 */
public class MapperBuilder {

    private static LoadingCache<Long, JsonMapper> cache = CacheBuilder
            .newBuilder().maximumSize(1000).build(new CacheLoader<Long, JsonMapper>() {
        @Override
        public JsonMapper load(Long key) throws Exception {
            return buildMapper(key);
        }
    });

    private static final JsonMapper def = MapperBuilder.create().build();

    public static JsonMapper getDefaultMapper() {
        return def;
    }

    private long features = JsonFeature.defaults();

    public MapperBuilder() {
    }

    public static MapperBuilder create() {
        return new MapperBuilder();
    }

    public MapperBuilder enable(JsonFeature jf) {
        features = jf.enable(features);
        return this;
    }

    public MapperBuilder disable(JsonFeature jf) {
        features = jf.disable(features);
        return this;
    }

    public MapperBuilder configure(JsonFeature jf, boolean state) {
        if (state)
            enable(jf);
        else
            disable(jf);
        return this;
    }

    public MapperBuilder configure(JsonFeature[] enabled, JsonFeature[] disabled) {
        for (JsonFeature jf : enabled) {
            enable(jf);
        }
        for (JsonFeature jf : disabled) {
            disable(jf);
        }
        return this;
    }

    public JsonMapper build() {
        try {
            return cache.get(features);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }


    public static JsonMapper buildMapper(long features) {

        ObjectMapper om = new ObjectMapper();

        for (JsonFeature jf : JsonFeature.values()) {
            configure(om, jf.getFeature(), jf.isEnabled(features));
        }

        return new JsonMapper(om);
    }

    private static void configure(ObjectMapper om, Object feature, boolean state) {
        if (feature instanceof SerializationFeature)
            om.configure((SerializationFeature) feature, state);
        else if (feature instanceof DeserializationFeature)
            om.configure((DeserializationFeature) feature, state);
        else if (feature instanceof JsonParser.Feature)
            om.configure((JsonParser.Feature) feature, state);
        else if (feature instanceof JsonGenerator.Feature)
            om.configure((JsonGenerator.Feature) feature, state);
        else if (feature instanceof MapperFeature)
            om.configure((MapperFeature) feature, state);
        else if (feature instanceof Include) {
            if (state) {
                om.setSerializationInclusion((Include) feature);
            }
        }
    }
}
