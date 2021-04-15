package com.worryfree.search.controller;

import com.worryfree.search.common.SearchConstant;
import com.worryfree.search.service.ESManagerService;
import com.worryfree.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/search/sku")
public class SearchController {



    @Autowired
    private ESManagerService esManagerService;

    @Autowired
    private SearchService searchService;

    //对搜索入参带有特殊符号进行处理

    public void handlerSearchMap(Map<String, String> paramMap){
        if (paramMap != null){
            Set<Map.Entry<String,String>> entries = paramMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                if (entry.getKey().startsWith(SearchConstant.SEARCH_PARAM_SPEC_PRE)){
                    paramMap.put(entry.getKey(),entry.getValue().replace("+","%2B"));
                }
            }
        }
    }
    /**
     * 全文检索
     * @return
     */
    @GetMapping
    public Map search(@RequestParam Map<String, String> paramMap) throws Exception {
        //特殊符号处理
        handlerSearchMap(paramMap);
        Map resultMap = searchService.search(paramMap);
        return resultMap;
    }
}
