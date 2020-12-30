package com.worryfree.service.goods.service;


import com.github.pagehelper.Page;
import com.worryfree.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BrandService {

    /**
     * 品牌列表查询
     */
    List<Brand> findList();

    /**
     * 根据id查询品牌数据
     */
    Brand findById(Integer id);

    /**
     * 品牌新增
     */

    void add(Brand brand);

    /**
     * 修改
     */
    void update(Brand brand);

    /**
     * 品牌删除
     */
    void deleteById(Integer id);

    /**
     * 品牌搜索
     */
    List<Brand>searchList(Map<String,Object> searchMap);

    /**
     * 分页查询
     */
    Page<Brand> findList(int page, int size);


    Page<Brand>  searchList(Map<String,Object> searchMap,int page,int size);


    /**
     * 根据方法名称进行查询品牌列表
     * @param categoryName
     * @return
     */

     List<Map> findBrandListByCategoryName(String categoryName);

}

