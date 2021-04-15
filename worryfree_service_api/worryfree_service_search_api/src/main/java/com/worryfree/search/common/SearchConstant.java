package com.worryfree.search.common;

/**
 * @author XM_Dong
 */
public final class SearchConstant {
    private SearchConstant(){};
    /***************************** 搜索参数 **********************/

    /**
     * 搜索关键字
     */
    public static String SEARCH_PARAM_KEYWORDS = "keywords";
    /**
     * 品牌
     */
    public static String SEARCH_PARAM_BRAND = "brand";
    /**
     * 规格前缀
     */
    public static String SEARCH_PARAM_SPEC_PRE = "spec_";
    /**
     * 价格
     */
    public static String SEARCH_PARAM_PRICE = "price";
    /**
     * 页码
     */
    public static String SEARCH_PARAM_PAGE_NUM = "pageNum";
    /**
     * 每页结果
     */
    public static String SEARCH_PARAM_PAGE_SIZE = "pageSize";
    /**
     * 排序方式
     */
    public static String SEARCH_PARAM_SORT_FIELD = "sortField";
    /**
     * 排序规则：ASC --> 升序 ，DESC -->降序
     */
    public static String SEARCH_PARAM_SORT_RULE = "sortRule";

    /***************************** es 搜索index **********************/

    /**
     * sku name
     */
    public static String ES_QUERY_INDEX_NAME = "name";
    /**
     * 品牌 name
     */
    public static String ES_QUERY_INDEX_BRAND = "brandName";
    /**
     * 规格  前缀
     */
    public static String ES_QUERY_INDEX_SPEC_PRE = "specMap.";
    /**
     * 规格  后缀
     */
    public static String ES_QUERY_INDEX_SPEC_POST = ".keyword";
    /**
     * 规格聚合检索
     */
    public static String ES_QUERY_INDEX_SPEC_KEYWORD = "spec.keyword";
    /**
     * 价格
     */
    public static String ES_QUERY_INDEX_PRICE = "price";
    /**
     * 升序
     */
    public static String ES_QUERY_SORT_ASC = "ASC";

    /**
     * 高亮状态前缀
     */
    public static String ES_HIGHLIGHT_PRE_TAGS = "<span style='color:red'>";
    /**
     * 高亮状态后缀
     */
    public static String ES_HIGHLIGHT_POST_TAGS = "</span>";


    /***************************** 聚合查询关键列表 **********************/


    /**
     * 品牌聚合
     */
    public static String ES_QUERY_AGGREGATION_SKU_BRAND = "skuBrand";
    /**
     * 规格聚合
     */
    public static String ES_QUERY_AGGREGATION_SKU_SPEC = "skuSpec";

    /***************************** 返回结果 key **********************/

    public static String ES_QUERY_RESULT_TOTAL= "total";
    public static String ES_QUERY_RESULT_Page_INFO= "pageInfo";
    public static String ES_QUERY_RESULT_TOTAL_PAGES= "totalPages";
    public static String ES_QUERY_RESULT_PAGE_NUM= "pageNum";

    public static String ES_QUERY_RESULT_ITEMS= "items";
    public static String ES_QUERY_RESULT_BRAND_LIST= "brandList";
    public static String ES_QUERY_RESULT_SPEC_LIST= "specList";

    public final static String DEFAULT_PAGE_SIZE = "30";
    public final static String DEFAULT_PAGE_NUM = "1";

    public final static Integer PRICE_LIST_LEN = 2;

}
