package com.worryfree.service.goods.dao;

import com.worryfree.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BrandMapper  extends Mapper<Brand> {

    /**
     * 根据分类名称进行查询品牌查询
     * @param categoryName
     * @return
     */
    @Select("SELECT id,name,image,letter FROM tb_brand WHERE id IN (SELECT brand_id FROM tb_category_brand WHERE category_id IN (SELECT category_id FROM tb_category WHERE `name`=#{categoryName}))")
   public List<Map> findBrandListByCategoryName(@Param("categoryName")String categoryName);

}
