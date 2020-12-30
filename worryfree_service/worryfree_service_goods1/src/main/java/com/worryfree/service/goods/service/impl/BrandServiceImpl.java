package com.worryfree.service.goods.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.worryfree.goods.pojo.Brand;
import com.worryfree.service.goods.dao.BrandMapper;
import com.worryfree.service.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {


    @Resource
    private BrandMapper brandMapper;

    /**
     * 品牌列表查询
     */
    @Override
    public List<Brand> findList() {
        List<Brand> brandsList = brandMapper.selectAll();
        return brandsList;
    }
    /**
     * 根据id查询品牌数据
     */
    @Override
    public Brand findById(Integer id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        return brand;
    }

    @Override
    @Transactional
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    @Override
    @Transactional
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    @Override
    public void deleteById(Integer id) {

        brandMapper.deleteByPrimaryKey(id);

    }


    @Override
    public List<Brand> searchList(Map<String, Object> searchMap) {

        Example example = new Example(Brand.class);

        Example.Criteria criteria = example.createCriteria();

        if (searchMap != null){
            String name = (String) searchMap.get("name");
            if (!StringUtil.isEmpty(name)){
                criteria.andLike("name","%" +name + "%");
            }
            String letter = (String) searchMap.get("letter");
            if (!StringUtil.isEmpty(letter)){
                criteria.andEqualTo("letter",letter);
            }
        }
        return brandMapper.selectByExample(example);
    }

    @Override
    public Page<Brand> findList(int page, int size) {

        PageHelper.startPage(page,size);

        return (Page<Brand>)brandMapper.selectAll();
    }

    @Override
    public Page<Brand> searchList(Map<String, Object> searchMap, int page, int size) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        if (searchMap != null){
            String name = (String) searchMap.get("name");
            if (!StringUtil.isEmpty(name)){
                criteria.andLike("name","%" +name + "%");
            }
            String letter = (String) searchMap.get("letter");
            if (!StringUtil.isEmpty(letter)){
                criteria.andEqualTo("letter",letter);
            }
        }
        PageHelper.startPage(page,size);
        return (Page<Brand>) brandMapper.selectByExample(example);
    }

    @Override
    public List<Map> findBrandListByCategoryName(String categoryName) {
        List<Map> listBrand = brandMapper.findBrandListByCategoryName(categoryName);
        return listBrand;
    }


}
