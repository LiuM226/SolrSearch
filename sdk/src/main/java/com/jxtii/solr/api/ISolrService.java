package com.jxtii.solr.api;


import com.jxtii.solr.entity.QueryCondition;
import com.jxtii.solr.entity.SolrContent;
import com.jxtii.solr.entity.SolrResult;

/**
 * RPC API 接口
 * Created by guolf on 17/7/12.
 */
public interface ISolrService {

    public SolrResult createIndex(SolrContent content);

    public SolrResult createIndexNoFile(SolrContent content);

    public SolrResult query(QueryCondition condition);

    public void deleteById(String id) throws Exception;


}
