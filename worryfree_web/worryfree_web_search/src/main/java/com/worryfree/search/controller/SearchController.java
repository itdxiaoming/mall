package com.worryfree.search.controller;


import com.worryfree.search.common.SearchConstant;
import com.worryfree.search.feign.SearchFegin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.util.Set;

@Controller
public class SearchController {

    @Autowired
    private SearchFegin searchFegin;

    @GetMapping("search")
    public String search(@RequestParam Map<String, String> paramMap, Model model){
        handlerSearchMap(paramMap);
        Map searchMap = searchFegin.search(paramMap);
        System.out.println(searchMap);
        model.addAttribute("paramMap",paramMap);
        model.addAttribute("resultMap",searchMap);
        return "search";
    }


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
}
