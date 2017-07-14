package com.jxtii.solr.request;

import com.jxtii.solr.entity.SolrContent;
import com.jxtii.solr.entity.QueryCondition;
import com.jxtii.solr.entity.SolrResult;
import com.jxtii.solr.utils.GsonUtil;
import com.jxtii.solr.utils.HandleEntity;
import com.jxtii.solr.utils.OkHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SDK调用封装类
 * Created by guolf on 17/7/7.
 */
public class SolrRequestService<T> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String baseUrl = "http://localhost:8090/";

    private static SolrRequestService service;

    private SolrRequestService() {
    }

    public static SolrRequestService getInstance() {
        if (service == null) {
            service = new SolrRequestService();
        }
        return service;
    }

    /**
     * 创建索引
     * @param entity
     * @return 成功返回OK，失败返回错误信息
     */
    public String createIndex(T entity) {
        HandleEntity<T> handleEntity = new HandleEntity<>();
        handleEntity.setData(entity);

        SolrContent content = handleEntity.getContent(entity.getClass());
        try {
            return OkHttpUtil.post(this.baseUrl.concat("createIndex.action"), GsonUtil.getGson().toJson(content));
        } catch (Exception ex) {
            logger.error(ex.toString());
            return ex.toString();
        }
    }

    /**
     * 根据条件进行查询，并转成实体类
     * @param condition 查询条件
     * @param cls 实体类型
     * @return
     */
    public SolrResult query(QueryCondition condition, Class<T> cls) {
        try {
            String json = OkHttpUtil.get(this.baseUrl.concat("query.action"), condition);
            SolrResult result = GsonUtil.fromJsonList(json, cls);
            return result;
        } catch (Exception ex) {
            SolrResult result = new SolrResult();
            result.setCode(500);
            result.setMsg(ex.toString());
            logger.error(ex.toString());
            return result;
        }
    }

    /**
     * 根据ID删除索引
     * @param id 索引ID
     * @return 成功返回OK，失败返回错误信息
     */
    public String deleteById(String id){
        try {
           return OkHttpUtil.post(this.baseUrl.concat("deleteById.action"), id);
        }catch (Exception ex){
            logger.error(ex.toString());
            return ex.toString();
        }
    }

    /**
     * 获取接口服务器地址
     * @return
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 设置接口服务器地址
     * @param baseUrl
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
