package com.jxtii.solr.entity;


import java.io.Serializable;

/**
 * 查询条件
 * Created by guolf on 17/7/3.
 */
public class QueryCondition implements Serializable {
    private String show;
    private String from;
    private String keywords;
    //    private Criteria criteria;
    private int start;
    private int rows;

    /**
     * 需要查询的字段,多字段用英文逗号分隔
     */
    public String getFrom() {
        return from;
    }

    /**
     * 显示的字段,多字段用英文逗号分隔
     */
    public String getShow() {
        return show;
    }

    /**
     * 设置需要查询字段，多字段用英文逗号分隔，与keywords组合使用
     *
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 设置需要显示的字段，多字段用英文逗号分隔
     *
     * @param show
     */
    public void setShow(String show) {
        this.show = show;
    }

    /**
     * 关键字
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * 设置需要查询的关键字，简单查询使用该方法，多条件组合查询使用
     *
     * @param keywords
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * 开始数量
     */
    public int getStart() {
        return start;
    }

    /**
     * 设置开始数量
     *
     * @param start
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * 每页数量
     */
    public int getRows() {
        return rows;
    }

    /**
     * 设置每页数量
     *
     * @param rows
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public String toString() {
        return "QueryCondition{" +
                "show='" + show + '\'' +
                ", from='" + from + '\'' +
                ", keywords='" + keywords + '\'' +
                ", start=" + start +
                ", rows=" + rows +
                '}';
    }
}
