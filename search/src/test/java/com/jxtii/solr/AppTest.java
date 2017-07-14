//package com.jxtii.solr;
//
//import com.jxtii.solr.service.SolrService;
//import org.apache.solr.client.solrj.SolrClient;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.solr.core.SolrTemplate;
//import org.springframework.data.solr.core.query.Criteria;
//import org.springframework.data.solr.core.query.Crotch;
//import org.springframework.data.solr.core.query.SimpleQuery;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * Unit test for simple App.
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class AppTest {
//
//    @Autowired
//    private SolrService solrService;
//
//    @Autowired
//    private SolrClient solrClient;
//
//    @Autowired
//    private SolrTemplate solrTemplate;
//
//    @Test
//    public void solrTest() {
////        SolrResult result =  solrService.query(new QueryCondition());
//
//        Criteria crotch = Crotch.where("id").contains("sss");
//
//        SimpleQuery groupQuery = new SimpleQuery(crotch);
//
////        Page map = solrTemplate.queryForPage(groupQuery, SolrContent.class);
//
//
//        System.out.println("result = " + crotch.getPredicates().toString());
//    }
//
//}
