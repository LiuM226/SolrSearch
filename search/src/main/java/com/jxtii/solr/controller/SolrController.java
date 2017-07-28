package com.jxtii.solr.controller;

import com.google.gson.Gson;
import com.jxtii.solr.entity.SolrContent;
import com.jxtii.solr.entity.QueryCondition;
import com.jxtii.solr.entity.SolrResult;
import com.jxtii.solr.service.SolrService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

/**
 * Created by guolf on 17/7/1.
 */
@RestController
public class SolrController {

    @Autowired
    private SolrService solrService;

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 查询
     * http://localhost:8080/query.action?show=id,attr_text&from=attr_text&keywords=%E6%AD%A3%E6%96%87
     *
     * @param condition 查询条件
     * @return
     */
    @ApiOperation(value = "根据条件进行查询", notes = "根据多关键字查询：keywords=attr_field3:field3 and id:*a*")
    @GetMapping("/query.action")
    public ResponseEntity query(@ModelAttribute QueryCondition condition) {
//        if (condition == null || StringUtils.isEmpty(condition.getKeywords())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("查询条件不能为空");
//        }
        logger.info(condition.getKeywords());

        if(!StringUtils.isEmpty(condition.getKeywords())){
            try {
                condition.setKeywords(URLDecoder.decode(condition.getKeywords(),"UTF-8"));
            }catch (Exception ex){
                logger.error(ex);
            }
        }
        logger.info(condition.getKeywords());
        SolrResult result = solrService.query(condition);
        return ResponseEntity.ok(new Gson().toJson(result));
    }

    @ApiOperation(value = "创建索引", notes = "{\"id\":\"2e37bb28-5dd3-47b6-9645-ba7f6c6794c6\",\"filePath\":\"http://xxx.com/aa.doc\",\"data\":{\"field1\":\"field1\",\"field3\":\"field3\",\"field2\":\"field2\"}}")
    @PostMapping("/createIndex.action")
    public ResponseEntity createIndex(@RequestBody String json) {
        logger.info(json);
        Gson gson = new Gson();
        SolrContent content = gson.fromJson(json, SolrContent.class);
        if (content.getId().isEmpty()) {
            logger.error("ID不能为空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID不能为空");
        }
        SolrResult result = solrService.createIndexWithFile(content);
        logger.info(result);
        if (result.getCode() == 200) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(result.getMsg());
        }
    }

    @ApiOperation(value = "根据ID删除索引")
    @PostMapping("/deleteById.action")
    public ResponseEntity del(@RequestBody String id) {
        try {
            solrService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.toString());
        }
    }

}
