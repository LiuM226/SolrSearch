package com.jxtii.solr;

import com.jxtii.solr.entity.SolrContent;
import com.jxtii.solr.entity.TestContent;
import com.jxtii.solr.utils.HandleEntity;
import com.jxtii.solr.utils.MQConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * MQ创建索引测试
     */
    @Test
    public void testCreateIndexByMQ() {
        TestContent testContent = new TestContent();
        testContent.setNewsId("baidu");
        testContent.setNewsTitle("测试标题");
        testContent.setNewContent("测试内容");
        testContent.setFilePath("http://baidu.com");

        HandleEntity handleEntity = new HandleEntity();
        handleEntity.setData(testContent);
        SolrContent solrContent =  handleEntity.getContent(TestContent.class);

        jmsTemplate.convertAndSend(MQConstant.MQ_RECEIVE_INDEX, solrContent);
    }

}
