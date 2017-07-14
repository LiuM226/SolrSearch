package com.jxtii.solr.utils;

import com.jxtii.solr.annotation.FieldType;
import com.jxtii.solr.annotation.SolrField;
import com.jxtii.solr.entity.SolrContent;
import org.joda.time.JodaTimePermission;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 将实体转换成统一类型
 * @date 17/7/8.
 * @author guolf
 */
public class HandleEntity<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public SolrContent getContent(Class<?> cls) {
        SolrContent content = new SolrContent();
        Map map = new HashMap<>();
        while (cls != null) {
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs) {
                SolrField solrField = f.getAnnotation(SolrField.class);
                if (solrField != null) {
                    if (solrField.type() == FieldType.NORMAL) {
                        Object value = Reflections.invokeGetter(getData(), f.getName());
                        if(f.getType() == Date.class){
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            map.put(f.getName(), simpleDateFormat.format(value));
                        } else {
                            map.put(f.getName(), value);
                        }
                    } else if (solrField.type() == FieldType.PRIMARY) {
                        String value = Reflections.invokeGetter(getData(), f.getName()) + "";
                        content.setId(value);
                    } else if (solrField.type() == FieldType.FILE) {
                        String value = Reflections.invokeGetter(getData(), f.getName()) + "";
                        content.setFilePath(value);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        content.setData(map);
        return content;
    }
}
