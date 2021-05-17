package com.yxx.mall.product.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.BrandEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.common.validated.AddGroup;
import com.yxx.mall.common.validated.EditGroup;
import com.yxx.mall.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-13
 */
@RestController
@RequestMapping("/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 查询品牌列表
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam("pageNum") Integer pageNum,
                  @RequestParam("pageSize") Integer pageSize,
                  BrandEntity brand){
        PageHelper.startPage(pageNum, pageSize);
        List<BrandEntity> list= brandService.listByBrand(brand);
        PageInfo page=new PageInfo(list);
        return R.ok().put("data",page);
    }

    /**
     * 添加品牌
     * @param brand
     * @return
     */
    @PostMapping("/add")
    public R add(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 查询品牌详情
     * @param brandId
     * @return
     */
    @GetMapping("/{brandId}")
    public R info(@PathVariable("brandId")Long brandId){
        BrandEntity brand = brandService.getById(brandId);
        return R.ok().put("data",brand);
    }

    /**
     * 根据ID修改品牌信息
     * @param brand
     * @return
     */
    @PutMapping("/edit")
    public R edit(@Validated({EditGroup.class}) @RequestBody BrandEntity brand){
        boolean b = brandService.updateById(brand);
        return b==true?R.ok():R.error();
    }

    /**
     * 批量删除品牌
     * @param brandIds
     * @return
     */
    @DeleteMapping("/delete/{brandIds}")
    public R delete(@PathVariable("brandIds") Long[] brandIds){
        brandService.batchRemove(brandIds);
        return R.ok();
    }
}