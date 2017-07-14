package com.jxtii.solr.mq;

import com.jxtii.solr.entity.SolrContent;
import com.jxtii.solr.entity.SolrResult;
import com.jxtii.solr.service.SolrService;
import com.jxtii.solr.utils.MQConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by guolf on 17/7/3.
 */
@Component
public class Receiver {

    @Autowired
    private SolrService solrService;

    @JmsListener(destination = MQConstant.MQ_RECEIVE_INDEX, containerFactory = "myFactory")
    public void receiveCreateIndexMessage(SolrContent Content) {
        System.out.println("Received <" + Content + ">");
        solrService.createIndexWithFile(Content);
    }

    @JmsListener(destination = MQConstant.MQ_RECEIVE_RESULT, containerFactory = "myFactory")
    public void receiveResultMessage(SolrResult result) {
        System.out.println("Received <" + result + ">");
    }
}
