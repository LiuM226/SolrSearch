package com.jxtii.solr.service;

import com.jxtii.solr.api.ISolrService;
import com.jxtii.solr.entity.QueryCondition;
import com.jxtii.solr.entity.SolrContent;
import com.jxtii.solr.entity.SolrResult;
import com.jxtii.solr.utils.MQConstant;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.AbstractUpdateRequest;
import org.apache.solr.client.solrj.request.ContentStreamUpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ContentStream;
import org.apache.solr.common.util.ContentStreamBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by guolf on 17/7/3.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service(version = "1.0.0")
public class SolrService implements ISolrService {

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private JmsTemplate jmsTemplate;

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 创建索引，含文件内容
     *
     * @param content 内容
     */
    @Override
    public SolrResult createIndex(SolrContent content) {
        SolrResult result;
        try {
            if (StringUtils.isEmpty(content.getFilePath()) || !content.getFilePath().startsWith("http")) {
                return createIndexNoFile(content);
            } else {
                ContentStreamUpdateRequest up = new ContentStreamUpdateRequest("/update/extract");
                if (!StringUtils.isEmpty(content.getFilePath())) {
                    URL url = new URL(content.getFilePath());
                    ContentStream stream = new ContentStreamBase.URLStream(url);
                    up.addContentStream(stream);
                }
                up.setParam("literal.id", content.getId());
                up.setParam("uprefix", "attr_");  // 将文本抽取产生的未在schema中定义的域加上前缀attr_
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (content.getData() != null) {
                    Map<String, Object> map = content.getData();
                    for (String key : map.keySet()) {
                        up.setParam("literal." + key, map.get(key) + "");
                    }
                }
                // 索引时间
                up.setParam("literal.add_time", simpleDateFormat.format(new Date()));
                up.setParam("fmap.content", "text");
                up.setAction(AbstractUpdateRequest.ACTION.COMMIT, true, true);
                solrClient.request(up);
                result = new SolrResult();
            }
        } catch (Exception ex) {
            logger.error(ex);
            result = new SolrResult(content, ex.toString());
        }
        return result;
    }

    /**
     * 通过MQ方式创建索引
     *
     * @param content
     */
    public void createIndexByMQ(SolrContent content) {
        SolrResult result = createIndex(content);
        jmsTemplate.convertAndSend(MQConstant.MQ_RECEIVE_RESULT, result);
    }

    /**
     * 创建索引，无文件
     *
     * @param content
     */
    @Override
    public SolrResult createIndexNoFile(SolrContent content) {
        SolrResult solrResult = null;
        try {
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", content.getId());
            if (content.getData() != null) {
                Map<String, Object> map = content.getData();
                for (String key : map.keySet()) {
                    document.addField("attr_" + key, map.get(key));
                }
            }
            UpdateResponse response = solrClient.add(document);
            solrResult = new SolrResult();
            System.out.println("response = " + response);
            solrClient.commit();
        } catch (Exception ex) {
            logger.error(ex);
            solrResult = new SolrResult(content, ex.toString());
        }
        return solrResult;
    }

    /**
     * 根据条件进行查询
     *
     * @param condition
     */
    @Override
    public SolrResult query(QueryCondition condition) {
        try {
            SolrParams params = getSolrParams(condition);
            QueryResponse response = solrClient.query(params);
            SolrDocumentList documentList = response.getResults();
            SolrResult solrResult = new SolrResult();
            solrResult.setCode(0);
            solrResult.setMsg("OK");
            solrResult.setTotal(documentList.getNumFound());
            solrResult.setStart(documentList.getStart());
            List<Map<String, String>> list = new ArrayList();
            for (SolrDocument entries : documentList) {
                Map map = new HashMap();
                for (String s : entries.keySet()) {
                    map.put(replaceAttr(s), entries.get(s).toString().replace("[", "").replace("]", ""));
                }
                list.add(map);
            }
            solrResult.setData(list);
            return solrResult;
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }

    public SolrResult queryForObject(QueryCondition condition, Class cls) {
        try {
            SolrParams params = getSolrParams(condition);
            QueryResponse response = solrClient.query(params);
            SolrDocumentList documentList = response.getResults();
            SolrResult solrResult = new SolrResult();
            solrResult.setCode(0);
            solrResult.setMsg("OK");
            solrResult.setTotal(documentList.getNumFound());
            solrResult.setStart(documentList.getStart());
            List<Map<String, String>> list = new ArrayList();
            for (SolrDocument entries : documentList) {
                Map map = new HashMap();
                for (String s : entries.keySet()) {
                    map.put(replaceAttr(s), entries.get(s).toString().replace("[", "").replace("]", ""));
                }
                list.add(map);
            }
            solrResult.setData(list);
            return solrResult;
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }

    /**
     * 根据ID删除
     *
     * @param id
     */
    @Override
    public void deleteById(String id) throws Exception {
        solrClient.deleteById(id);
        solrClient.commit();
    }

    /**
     * 构造查询参数
     *
     * @param condition
     * @return
     */
    private static SolrParams getSolrParams(QueryCondition condition) {
        ModifiableSolrParams params = new ModifiableSolrParams();
        params.add("defType", "edismax");
        if (StringUtils.isEmpty(condition.getKeywords())) {
            params.add("q", "*:*");
        } else {
            params.add("q", condition.getKeywords());
        }
        if (!StringUtils.isEmpty(condition.getShow())) {
            params.add("fl", addAttr(condition.getShow())); // 指定要显示的字段
        }
        // 指定需要查询的字段
        if (condition.getKeywords() == null || condition.getKeywords().indexOf(":") < -1) {
            params.add("qf", StringUtils.isEmpty(condition.getFrom()) ? "attr_text" : condition.getFrom());
        }

        if (condition.getStart() > 0) {
            params.add("start", String.valueOf(condition.getStart()));
        }
        if (condition.getRows() > 0) {
            params.add("rows", String.valueOf(condition.getRows()));

        }
        return params;
    }

    /**
     * 替换属性字段
     *
     * @param value
     * @return
     */
    private static String replaceAttr(String value) {
        StringBuffer stringBuffer = new StringBuffer();
        if (!StringUtils.isEmpty(value)) {
            for (String s : value.split(",")) {
                stringBuffer.append(s.replace("attr_", "")).append(",");
            }
        }
        return stringBuffer.toString().substring(0, stringBuffer.length() - 1);
    }

    /**
     * 统一添加attr_
     *
     * @param value
     * @return
     */
    private static String addAttr(String value) {
        StringBuffer stringBuffer = new StringBuffer();
        if (!StringUtils.isEmpty(value)) {
            for (String s : value.split(",")) {
                if (s.equals("id") || s.equals("*")) {
                    stringBuffer.append(s).append(",");
                } else {
                    stringBuffer.append("attr_".concat(s)).append(",");
                }
            }
        }
        return stringBuffer.toString().substring(0, stringBuffer.length() - 1);
    }

}
