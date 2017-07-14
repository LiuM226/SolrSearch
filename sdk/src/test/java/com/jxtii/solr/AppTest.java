package com.jxtii.solr;

import com.jxtii.solr.entity.QueryCondition;
import com.jxtii.solr.entity.SolrResult;
import com.jxtii.solr.entity.TestContent;
import com.jxtii.solr.query.Criteria;
import com.jxtii.solr.query.Crotch;
import com.jxtii.solr.query.QueryParser;
import com.jxtii.solr.request.SolrRequestService;
import com.jxtii.solr.utils.GsonUtil;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.List;

/**
 * solr查询SDK单元测试
 */
public class AppTest extends TestCase {

    /**
     * 查询条件单元测试1
     */
    public void testCondition1() {
        Criteria crotch = Crotch.where("id").contains("sss").and("title").in("标题");
        QueryParser parser = new QueryParser();
        Assert.assertEquals(parser.createQueryString(crotch), "id:*sss* AND title:标题");
    }

    /**
     * 查询条件单元测试2
     */
    public void testCondition2() {
        Criteria crotch1 = Crotch.where("id").contains("sss").and("title").in("标题");
        Criteria crotch2 = Crotch.where("id").contains("sss").and("title").startsWith("标题");
        QueryParser parser = new QueryParser();
        Assert.assertEquals(parser.createQueryString(crotch1.connect().or(crotch2)),
                "(id:*sss* AND title:标题) OR (id:*sss* AND title:标题*)");
    }

    /**
     * 创建索引单元测试
     */
    public void testCreateIndex() {
        SolrRequestService<TestContent> requestService = SolrRequestService.getInstance();
        TestContent testContent = new TestContent();
        testContent.setNewsId("news0709003");
        testContent.setNewsTitle("测试标题");
        testContent.setNewContent("测试内容");
        requestService.setBaseUrl("http://localhost:8080/");
        String result = requestService.createIndex(testContent);
        Assert.assertEquals("OK", result);
    }

    /**
     * 创建含文件类型索引
     */
    public void testCreateIndexWithFile() {
        SolrRequestService<TestContent> requestService = SolrRequestService.getInstance();
        TestContent testContent = new TestContent();
        testContent.setNewsId("news0709004");
        testContent.setNewsTitle("测试标题");
        testContent.setNewContent("测试内容");
        testContent.setFilePath("http://www.baidu.com");
        requestService.setBaseUrl("http://localhost:8080/search/");
        String result = requestService.createIndex(testContent);
        Assert.assertEquals("OK", result);
    }

    /**
     * 查询单元测试
     */
    public void testQuery1() {
        SolrRequestService<TestContent> requestService = SolrRequestService.getInstance();
        requestService.setBaseUrl("http://localhost:8090/search/");
        Criteria crotch = Criteria.where("id").in("news0709004");
        QueryCondition condition = new QueryCondition();
        condition.setKeywords(new QueryParser().createQueryString(crotch));
        SolrResult<List<TestContent>> solrResult = requestService.query(condition, TestContent.class);
        Assert.assertEquals(solrResult.getData().get(0).getNewsId(), "news0709004");
    }

    public void testQuery2() {
        SolrRequestService<TestContent> requestService = SolrRequestService.getInstance();
        requestService.setBaseUrl("http://localhost:8080/");
        Criteria crotch = Crotch.where("id").in("news0709005");
        QueryCondition condition = new QueryCondition();
        condition.setKeywords(new QueryParser().createQueryString(crotch));
        SolrResult<List<TestContent>> solrResult = requestService.query(condition, TestContent.class);
        Assert.assertEquals(solrResult.getTotal(), 0);
    }

    public void testQuery3() {
        SolrRequestService<TestContent> requestService = SolrRequestService.getInstance();
        requestService.setBaseUrl("http://localhost:8080/");
        Criteria crotch = Criteria.where("entityName").in("Article");
        QueryCondition condition = new QueryCondition();
        condition.setKeywords(new QueryParser().createQueryString(crotch));
        condition.setFrom("entityName");
        SolrResult solrResult = requestService.query(condition, TestContent.class);
        System.out.println("solrResult = " + solrResult);
    }

    public void testGson() {
        String json = " {\"code\":0,\"msg\":\"OK\",\"data\":[{\"newsTitle\":\"abc\"}],\"total\":1,\"start\":0}";
        SolrResult result = GsonUtil.fromJsonList(json, TestContent.class);
        Assert.assertEquals(result.getTotal(), 1);
        Assert.assertEquals(((List<TestContent>) result.getData()).get(0).getNewsTitle(), "abc");
    }

    /**
     * 删除索引
     */
    public void testDelete() {
        SolrRequestService<TestContent> requestService = SolrRequestService.getInstance();
        requestService.setBaseUrl("http://localhost:8080/");
        String result = requestService.deleteById("1");
        Assert.assertEquals(result, "OK");
    }

}
