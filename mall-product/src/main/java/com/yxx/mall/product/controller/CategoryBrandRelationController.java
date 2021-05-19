package com.yxx.mall.product.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.CategoryBrandRelationEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.CategoryBrandRealtionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author xyong
 * date 2021-05-19
 */
@RestController
@RequestMapping("/product/relation")
public class CategoryBrandRelationController {

    @Autowired
    CategoryBrandRealtionService categoryBrandRealtionService;


    /**
     * 查询品牌下的关联关系
     * @param pageNum
     * @param pageSize
     * @param brandId
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam("pageNum")Integer pageNum,
                  @RequestParam("pageSize")Integer pageSize
            , Long brandId){
        PageHelper.startPage(pageNum,pageSize);
        List<CategoryBrandRelationEntity> list= categoryBrandRealtionService.listByBrandId(brandId);
        PageInfo page=new PageInfo(list);
        return R.ok().put("data",page);
    }

    /**
     * 新增关联关系
     * @param categoryBrandRelationEntity
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelationEntity){
        categoryBrandRealtionService.saveDetail(categoryBrandRelationEntity);
        return R.ok();
    }

    /**
     * 根据ID删除关联关系
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable("ids") Long[] ids){
        categoryBrandRealtionService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
