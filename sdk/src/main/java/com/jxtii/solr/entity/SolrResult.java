package com.jxtii.solr.entity;

import java.io.Serializable;

/**
 * solr结果封装类
 * Created by guolf on 17/7/3.
 */
public class SolrResult<T> implements Serializable {
    private int code;
    private String msg;
    private T data;
    private long total;
    private long start;

    public SolrResult() {
        this.code = 200;
        this.msg = "OK";
    }

    public SolrResult(T data) {
        this.code = 200;
        this.msg = "OK";
        this.data = data;
    }

    public SolrResult(T data, String msg) {
        this.code = 500;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 状态值，200成成，其余为失败
     */
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 消息内容，成功为OK，否则为具体错误信息
     */
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 返回的结果集
     */
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 数据总数
     */
    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 开始数量
     */
    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    @Override
    public String toString() {
        return "SolrResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", total=" + total +
                ", start=" + start +
                '}';
    }
}
