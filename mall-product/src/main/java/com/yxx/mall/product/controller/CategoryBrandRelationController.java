package com.yxx.mall.product.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.BrandEntity;
import com.yxx.mall.common.entity.product.CategoryBrandRelationEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.CategoryBrandRealtionService;
import com.yxx.mall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xyong
 * date 2021-05-19
 */
@RestController
@RequestMapping("/product/categorybrandrelation")
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

    @GetMapping("/brands/list")
    public R relationBrandsList(@RequestParam("catId") Long catId){
        List<BrandEntity> vos=categoryBrandRealtionService.getBrandsByCatId(catId);
        List<BrandVo> collect = vos.stream().map((item) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data",collect);
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
