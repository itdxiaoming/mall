package com.worryfree.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.worryfree.search.common.SearchConstant;
import com.worryfree.search.dao.ESManagerMapper;
import com.worryfree.search.pojo.SkuInfo;
import com.worryfree.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;


    @Override
    public Map<String,Object> search(Map<String, String> searchMap) {
        if (searchMap != null){
            NativeSearchQueryBuilder nativeSearchQueryBuilder = this.nativeSearchQueryBuildBySearchMap(searchMap);
            return this.searchResult(nativeSearchQueryBuilder,new ESPageInfo(searchMap));
        }
        return new HashMap<>();
    }


    private NativeSearchQueryBuilder nativeSearchQueryBuildBySearchMap(Map<String, String> searchMap){
        // 原生搜索实现类
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();

        //组合条件查询
        BoolQueryBuilder boolQueryBuilder = this.boolQueryBuildBySearchMap(searchMap);

        //传入组合查询条件
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);

        //5:高亮
        this.highlightBuildByNativeSearchQueryBuilderAndSearchMap(nativeSearchQueryBuilder);
//
//        //6. 品牌聚合(分组)查询
        this.brandAggregationBuildByNativeSearchQueryBuilderAndSearchMap(nativeSearchQueryBuilder);
//
//        //7. 规格聚合(分组)查询
        this.specAggregationBuildByNativeSearchQueryBuilderAndSearchMap(nativeSearchQueryBuilder);
//
//        //8: 排序
        this.sortBuildByNativeSearchQueryBuilderAndSearchMap(nativeSearchQueryBuilder,searchMap);


        //9: 分页
        this.pageBuildByNativeSearchQueryBuilderAndSearchMap(nativeSearchQueryBuilder,new ESPageInfo(searchMap));


        return nativeSearchQueryBuilder;
    }

    private void pageBuildByNativeSearchQueryBuilderAndSearchMap(NativeSearchQueryBuilder nativeSearchQueryBuilder, ESPageInfo esPageInfo) {
        nativeSearchQueryBuilder.withPageable(PageRequest.of(Integer.parseInt(esPageInfo.pageNum),Integer.parseInt(esPageInfo.pageSize)));
    }

    private void sortBuildByNativeSearchQueryBuilderAndSearchMap(NativeSearchQueryBuilder nativeSearchQueryBuilder, Map<String, String> searchMap) {

        String sortFiled = searchMap.get(SearchConstant.SEARCH_PARAM_SORT_FIELD);

        if (StringUtils.isNotEmpty(sortFiled)){

            String sortRule = searchMap.get(SearchConstant.SEARCH_PARAM_SORT_RULE);

            if (SearchConstant.ES_QUERY_SORT_ASC.equals(sortRule)){
                nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortFiled).order(SortOrder.ASC));
            }else {
                nativeSearchQueryBuilder.withSort(SortBuilders.fieldSort(sortFiled).order(SortOrder.DESC));
            }
        }

    }

    private void specAggregationBuildByNativeSearchQueryBuilderAndSearchMap(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder
                .addAggregation(AggregationBuilders
                        .terms(SearchConstant.ES_QUERY_AGGREGATION_SKU_SPEC)
                        .field(SearchConstant.ES_QUERY_INDEX_SPEC_KEYWORD));

    }

    private void brandAggregationBuildByNativeSearchQueryBuilderAndSearchMap(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder
                .addAggregation(AggregationBuilders
                        .terms(SearchConstant.ES_QUERY_AGGREGATION_SKU_BRAND)
                        .field(SearchConstant.ES_QUERY_INDEX_BRAND));

    }

    private void highlightBuildByNativeSearchQueryBuilderAndSearchMap(NativeSearchQueryBuilder nativeSearchQueryBuilder) {

        HighlightBuilder.Field field = new HighlightBuilder
                .Field(SearchConstant.ES_QUERY_INDEX_NAME)
                .requireFieldMatch(false)
                .preTags(SearchConstant.ES_HIGHLIGHT_PRE_TAGS)
                .postTags(SearchConstant.ES_HIGHLIGHT_POST_TAGS);

        nativeSearchQueryBuilder.withHighlightFields(field);
    }

    private Map<String ,Object> searchResult(NativeSearchQueryBuilder nativeSearchQueryBuilder ,ESPageInfo esPageInfo){

        HashMap<String, Object> resultMap = new HashMap<>();
        SearchHits<SkuInfo> searchHits = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), SkuInfo.class);

        if (searchHits != null){

            List<SkuInfo> skuInfoList = this.convertSkuList(searchHits);
            List<String> brandList = this.polymerizationByHitAndKeyToList(searchHits, SearchConstant.ES_QUERY_AGGREGATION_SKU_BRAND);
            List<String> spuList = this.polymerizationByHitAndKeyToList(searchHits, SearchConstant.ES_QUERY_AGGREGATION_SKU_SPEC);

            resultMap.put(SearchConstant.ES_QUERY_RESULT_Page_INFO,convertPageInfo(searchHits.getTotalHits(),esPageInfo));
            resultMap.put(SearchConstant.ES_QUERY_RESULT_ITEMS,skuInfoList);
            resultMap.put(SearchConstant.ES_QUERY_RESULT_BRAND_LIST,brandList);
            resultMap.put(SearchConstant.ES_QUERY_RESULT_SPEC_LIST,convertSpuList(spuList));

            return  resultMap;
        }


        return null;
    }

    private Map<String,String>convertPageInfo(Long totalHits,ESPageInfo esPageInfo){
        Map<String,String> resultMap = new HashMap<>();
        Long totalPage = 0L;
        if (totalHits > 0){
            long pageSize = Long.parseLong(esPageInfo.pageSize);
            totalPage = totalHits%pageSize;
            totalPage = totalPage == 0 ? totalHits/pageSize : (totalHits/pageSize ) + 1;
        }

        resultMap.put(SearchConstant.ES_QUERY_RESULT_TOTAL_PAGES,totalPage.toString());
        resultMap.put(SearchConstant.ES_QUERY_RESULT_PAGE_NUM,esPageInfo.getPageSize());
        resultMap.put(SearchConstant.ES_QUERY_RESULT_TOTAL,totalHits.toString());

        return resultMap;
    }
    private Map<String, Set<String>> convertSpuList(List<String> specList){
        Map<String, Set<String>> specMap = new HashMap<>();

        if (specList != null && specList.size() > 0){
            for (String spec : specMap.keySet()) {
                Map map = JSON.parseObject(spec, Map.class);
                Set<Map.Entry<String,String>> set = map.entrySet();
                for (Map.Entry<String, String> stringEntry : set) {

                    String key = stringEntry.getKey();
                    String value = stringEntry.getValue();

                    Set<String> specValue = specMap.get(key);
                    if (specValue == null){
                        specValue = new HashSet<>();
                    }
                    specValue.add(value);
                    specMap.put(key,specValue);
                }

            }
        }


        return specMap;
    }
    private List<String> polymerizationByHitAndKeyToList(SearchHits<SkuInfo> searchHits,String key){

        ParsedStringTerms brandTerms = searchHits.getAggregations().get(key);


        List<String> resultList = brandTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());

        return resultList;
    }
    private List<SkuInfo> convertSkuList(SearchHits<SkuInfo> searchHits){
        List<SearchHit<SkuInfo>> hits = searchHits.getSearchHits();

        List<SkuInfo> skuInfoList = new ArrayList<>();
        for (SearchHit<SkuInfo> skuInfoSearchHit : hits) {
            SkuInfo skuInfo = skuInfoSearchHit.getContent();

            Map<String, List<String>> highlightFields = skuInfoSearchHit.getHighlightFields();
            if (highlightFields.size() > 0 && highlightFields != null){
                skuInfo.setName(highlightFields.get(SearchConstant.ES_QUERY_INDEX_NAME).get(0));
            }
            skuInfoList.add(skuInfo);
        }


        return skuInfoList;
    }

    private BoolQueryBuilder boolQueryBuildBySearchMap(Map<String, String> searchMap){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //0:关键词
        this.buildKeyWordByBoolQueryBuildAndSearchMap(boolQueryBuilder,searchMap);

        //1:条件 品牌
        this.buildBrandByBoolQueryBuildAndSearchMap(boolQueryBuilder,searchMap);

        //2:条件 规格
        this.buildSpecByBoolQueryBuildAndSearchMap(boolQueryBuilder,searchMap);

        //3:条件 价格
        this.buildPriceByBoolQueryBuildAndSearchMap(boolQueryBuilder,searchMap);

        return boolQueryBuilder;
    }

    private void buildPriceByBoolQueryBuildAndSearchMap(BoolQueryBuilder boolQueryBuilder, Map<String, String> searchMap) {

        String price = searchMap.get(SearchConstant.SEARCH_PARAM_PRICE);
        if (StringUtils.isNotEmpty(price)){
            String[] priceList = price.split("-");

            boolQueryBuilder.filter(QueryBuilders.rangeQuery(SearchConstant.ES_QUERY_INDEX_PRICE).gte(priceList[0]));
            if (priceList.length == SearchConstant.PRICE_LIST_LEN){
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(SearchConstant.ES_QUERY_INDEX_PRICE).gte(priceList[1]));
            }
        }

    }

    private void buildSpecByBoolQueryBuildAndSearchMap(BoolQueryBuilder boolQueryBuilder, Map<String, String> searchMap) {

        for (String key : searchMap.keySet()) {
            if (key.startsWith(SearchConstant.SEARCH_PARAM_SPEC_PRE)){
                String specVal = searchMap.get(key).replace("%2B", "+");

                String esIndex = SearchConstant.ES_QUERY_INDEX_SPEC_PRE
                        + key.substring(SearchConstant.SEARCH_PARAM_SPEC_PRE.length())
                        + SearchConstant.ES_QUERY_INDEX_SPEC_POST;

                boolQueryBuilder.filter(QueryBuilders.termQuery(esIndex,specVal));

            }
        }


    }

    private void buildKeyWordByBoolQueryBuildAndSearchMap(BoolQueryBuilder boolQueryBuilder,Map<String, String> searchMap){

        String keyWord = searchMap.get(SearchConstant.SEARCH_PARAM_KEYWORDS);
        if (StringUtils.isNotEmpty(keyWord)){
            boolQueryBuilder.must(QueryBuilders.matchQuery(SearchConstant.ES_QUERY_INDEX_NAME,keyWord).operator(Operator.AND));
        }

    }

    private void buildBrandByBoolQueryBuildAndSearchMap(BoolQueryBuilder boolQueryBuilder,Map<String, String> searchMap){
        String brandName = searchMap.get(SearchConstant.SEARCH_PARAM_BRAND);
        if (StringUtils.isNotEmpty(brandName)){
            boolQueryBuilder.filter(QueryBuilders.termQuery(SearchConstant.ES_QUERY_INDEX_BRAND,brandName));
        }
    }




}

class ESPageInfo{
    public  String pageNum;
    public  String pageSize;

    public String getPageNum() {
        return pageNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public ESPageInfo(Map<String,String> paramMap) {
        String pageNum1 = paramMap.get(SearchConstant.SEARCH_PARAM_PAGE_NUM);
        String  pageSize1= paramMap.get(SearchConstant.SEARCH_PARAM_PAGE_SIZE);

        if (StringUtils.isEmpty(pageNum1)) {
            this.pageNum = SearchConstant.DEFAULT_PAGE_NUM;
        }

        if (StringUtils.isEmpty(pageSize1)) {
            this.pageSize = SearchConstant.DEFAULT_PAGE_SIZE;
        }
    }
}