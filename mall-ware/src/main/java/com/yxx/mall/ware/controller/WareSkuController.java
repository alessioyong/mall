package com.yxx.mall.ware.controller;

import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.WareSkuEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.ware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author xyong
 * date 2021-06-03
 */
@RestController
@RequestMapping("/ware/sku")
public class WareSkuController {

    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 查询商品库存信息
     * @param params
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params){
        PageInfo pageInfo=wareSkuService.getWareSkuList(params);
        return R.ok().put("data",pageInfo);
    }
    /**
     * 新增商品库存信息
     * @param skuEntity
     * @return
     */
    @PostMapping("/add")
    public R add(@RequestBody WareSkuEntity skuEntity){

        wareSkuService.save(skuEntity);
        return R.ok();
    }

    /**
     * 根据ID查询商品库存信息
     * @param id
     * @return
     */
    @GetMapping("/get/{id}")
    public R getInfo(@PathVariable("id") Long id){

        WareSkuEntity wareSkuEntity = wareSkuService.getById(id);
        return R.ok().put("data",wareSkuEntity);
    }

    /**
     * 根据id更新商品库存信息
     * @param skuEntity
     * @return
     */
    @PutMapping("/update")
    public R update(@RequestBody WareSkuEntity skuEntity){
        wareSkuService.updateById(skuEntity);
        return R.ok();
    }

    /**
     * 批量删除商品库存信息
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable("ids") Long []ids){
        wareSkuService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
}
