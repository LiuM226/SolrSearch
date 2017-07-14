package com.jxtii.solr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by guolf on 17/7/14.
 */
@Controller
public class IndexController {

    @GetMapping("/")
    @ResponseBody
    public String index(){
        return "欢迎使用全文检索组件";
    }

}
