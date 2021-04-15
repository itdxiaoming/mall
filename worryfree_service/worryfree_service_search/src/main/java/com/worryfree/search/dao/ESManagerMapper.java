package com.worryfree.search.dao;

import com.worryfree.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ESManagerMapper extends ElasticsearchRepository<SkuInfo,Long> {
}
