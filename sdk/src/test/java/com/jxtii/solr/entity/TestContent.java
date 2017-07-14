package com.jxtii.solr.entity;

import com.google.gson.annotations.SerializedName;
import com.jxtii.solr.annotation.FieldType;
import com.jxtii.solr.annotation.SolrField;

/**
 * Created by guolf on 17/7/9.
 */
public class TestContent {

    @SerializedName("id")
    @SolrField(type = FieldType.PRIMARY)
    private String newsId;

    @SolrField
    private String newsTitle;

    @SolrField
    private String newContent;

    @SolrField(type = FieldType.FILE)
    private String filePath;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "TestContent{" +
                "newsId='" + newsId + '\'' +
                ", newsTitle='" + newsTitle + '\'' +
                ", newContent='" + newContent + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
