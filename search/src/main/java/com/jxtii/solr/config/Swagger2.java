package com.jxtii.solr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by guolf on 17/7/6.
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
//http://localhost:8080/swagger-ui.html
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jxtii.solr"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("人大OA全文检索API")
                .description("人大OA全文检索API")
                .termsOfServiceUrl("http://www.jxtii.com/")
                .contact(new Contact("郭灵发","","18970026242@189.cn"))
                .version("1.0")
                .build();
    }

}