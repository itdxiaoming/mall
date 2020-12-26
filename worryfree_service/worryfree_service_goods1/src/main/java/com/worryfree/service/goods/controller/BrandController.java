package com.worryfree.service.goods.controller;
;
import com.github.pagehelper.Page;
import com.worryfree.common.pojo.PageResult;
import com.worryfree.common.pojo.Result;
import com.worryfree.common.pojo.StatusCode;
import com.worryfree.goods.pojo.Brand;
import com.worryfree.service.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public Result<List<Brand>> findList(){
        List<Brand> listBrand = brandService.findList();
        return new Result<>(true, StatusCode.OK ,"查询成功",listBrand);
    }

    /***
     * 根据ID查询品牌数据
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public Result findById(@PathVariable Integer id){
        Brand brand = brandService.findById(id);
        return new Result(true, StatusCode.OK ,"查询成功",brand);
    }


    @PostMapping
    public Result add(@RequestBody Brand brand){
        brandService.add(brand);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable Integer id){
        brandService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @PutMapping(value = "/{id}")
    public Result update(@PathVariable Integer id,@RequestBody Brand brand){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    @GetMapping(value = "/search")
    public Result<List<Brand>> search(@RequestParam Map<String,Object> searchMap){
        List<Brand> brands = brandService.searchList(searchMap);
        return new Result<>(true,StatusCode.OK,"查询成功",brands);
    }

    @GetMapping(value = "/{page}/{size}")
    public Result search(@PathVariable("page") Integer page,@PathVariable("size") Integer size){
        Page<Brand> pageList = brandService.findList(page,size);
        new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageList);
    }

    @GetMapping(value = "/search/{page}/{size}")
    public Result search(@RequestParam Map<String,Object> searchMap, @PathVariable("page") Integer page,@PathVariable("size") Integer size){
        Page<Brand> pageList = brandService.searchList(searchMap,page,size);
        new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageList);
    }




}
