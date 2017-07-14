package com.jxtii.solr.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

/**
 * Created by guolf on 17/7/1.
 */
@Configuration
@EnableSolrRepositories(schemaCreationSupport = true)
public class SolrConfig {

    @Value("${spring.data.solr.cores}")
    public String solrCores;

    public String getSolrCores() {
        return solrCores;
    }

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient(getSolrCores());
    }
}
