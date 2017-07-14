package com.jxtii.solr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class SolrApplication {

    public static void main(String[] args) {
        SpringApplication.run(SolrApplication.class, args);
    }
}
