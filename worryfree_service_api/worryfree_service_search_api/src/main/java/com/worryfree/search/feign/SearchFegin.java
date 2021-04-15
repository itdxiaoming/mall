package com.worryfree.search.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "search")
public interface SearchFegin {

    @GetMapping("/search/sku")
    Map search(@RequestParam Map<String, String> paramMap);
}
