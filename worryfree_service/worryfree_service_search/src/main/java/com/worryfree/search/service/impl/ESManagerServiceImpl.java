package com.worryfree.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.worryfree.goods.feign.SkuFegin;
import com.worryfree.goods.pojo.Sku;
import com.worryfree.search.dao.ESManagerMapper;
import com.worryfree.search.pojo.SkuInfo;
import com.worryfree.search.service.ESManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ESManagerServiceImpl implements ESManagerService {

    @Autowired
    private  ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private SkuFegin skuFegin;
    @Autowired
    ESManagerMapper esManagerMapper;
    @Override
    public void createMappingAndIndex() {
        elasticsearchRestTemplate.createIndex(SkuInfo.class);
        elasticsearchRestTemplate.putMapping(SkuInfo.class);
//        elasticsearchRestTemplate.createIndex(SkuInfo.class);
    }

    @Override
    public void importAll() {

        List<Sku> skuList = skuFegin.findSkuListBySpuId("all");

        importDataToES(skuList);
    }

    @Override
    public void importDataBySpuId(String spuId) {
        List<Sku> skuList = skuFegin.findSkuListBySpuId(spuId);

        importDataToES(skuList);
    }

    private void importDataToES(List<Sku> skuList ){
        if (skuList == null || skuList.size() <= 0){
            throw new  RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        String jsonSkuList = JSON.toJSONString(skuList);
        List<SkuInfo> skuInfos = JSON.parseArray(jsonSkuList, SkuInfo.class);
        for(SkuInfo skuInfo : skuInfos){
            Map specMap = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(specMap);
        }
        esManagerMapper.saveAll(skuInfos);
    }

    @Override
    public void delDataBySpuId(String spuId) {
        List<Sku> skuList = skuFegin.findSkuListBySpuId(spuId);
        if (skuList == null || skuList.size()<=0){
            throw new RuntimeException("当前没有数据被查询到,无法导入索引库");
        }
        for (Sku sku : skuList) {
            esManagerMapper.deleteById(Long.parseLong(sku.getId()));
        }
    }
}
