package com.jxtii.solr.entity;

import java.io.Serializable;
import java.util.Map;

/**
 * solr内容封装类
 * Created by guolf on 17/7/6.
 */
public class SolrContent implements Serializable {

    private String id;
    private String filePath;

    private Map<String, Object> data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SolrContent{" +
                "id='" + id + '\'' +
                ", filePath='" + filePath + '\'' +
                ", data=" + data +
                '}';
    }
}
