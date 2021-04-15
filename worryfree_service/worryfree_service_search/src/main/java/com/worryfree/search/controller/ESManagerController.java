package com.worryfree.search.controller;

import com.worryfree.common.pojo.Result;
import com.worryfree.common.pojo.StatusCode;
import com.worryfree.search.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("manager")
public class ESManagerController {

    @Autowired
    private ESManagerService esManagerService;


    //创建索引库结构
    @GetMapping("/create")
    public Result create(){
        esManagerService.createMappingAndIndex();
        return new Result(true, StatusCode.OK,"创建索引库结构成功");
    }
    //导入全部数据
    @GetMapping("/importAll")
    public Result importAll(){
        esManagerService.importAll();
        return new Result(true, StatusCode.OK,"导入全部数据成功");
    }

}
