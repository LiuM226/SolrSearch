package com.jxtii.solr.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jxtii.solr.entity.SolrResult;
import com.jxtii.solr.reflect.TypeBuilder;

import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON序列号工具
 * @date 17/7/8.
 * @author guolf
 */
public class GsonUtil {

    private static Gson gson = new GsonBuilder().create();

    public static Gson getGson() {
        return gson;
    }

    /**
     * json转List对象
     *
     * @param json  json内容
     * @param clazz 实体类型
     * @param <T>   返回SolrResult内容
     * @return
     */
    public static <T> SolrResult<List<T>> fromJsonList(String json, Class<T> clazz) {
        Type type = TypeBuilder.newInstance(SolrResult.class)
                .beginSubType(List.class)
                .addTypeParam(clazz)
                .endSubType()
                .build();
        return new Gson().fromJson(json, type);
    }


    /**
     * json转Object对象
     *
     * @param json  json内容
     * @param clazz 实体类型
     * @param <T>   返回SolrResult内容
     * @return
     */
    public static <T> SolrResult<T> fromJsonObject(String json, Class<T> clazz) {
        Type type = TypeBuilder.newInstance(SolrResult.class)
                .addTypeParam(clazz)
                .build();
        return new Gson().fromJson(json, type);
    }

}
