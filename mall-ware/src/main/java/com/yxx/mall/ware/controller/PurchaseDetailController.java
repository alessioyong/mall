package com.yxx.mall.ware.controller;

import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.PurchaseDetailEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.ware.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author xyong
 * date 2021-06-24
 */
@RestController
@RequestMapping("/ware/purchasedetail")
public class PurchaseDetailController {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    /**
     * 分页条件查询采购需求列表
     * @param parmas
     * @return
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> parmas){
        PageInfo pageInfo=purchaseDetailService.listByConditions(parmas);
        return R.ok().put("data",pageInfo);
    }

    /**
     * 添加采购需求
     * @param purchaseDetail
     * @return
     */
    @PostMapping("/add")
    public R add(@RequestBody PurchaseDetailEntity purchaseDetail){
        purchaseDetailService.save(purchaseDetail);
        return R.ok();
    }

    /**
     * 根据Id查询采购需求
     * @param id
     * @return
     */
    @GetMapping("/getInfo/{id}")
    public R getInfo(@PathVariable("id") Long id){
        PurchaseDetailEntity byId = purchaseDetailService.getById(id);
        return R.ok().put("data",byId);
    }

    /**
     * 根据ID修改采购需求
     * @param purchaseDetailEntity
     * @return
     */
    @PutMapping("/edit")
    public R edit(@RequestBody PurchaseDetailEntity purchaseDetailEntity){
        purchaseDetailService.updateById(purchaseDetailEntity);
        return R.ok();
    }

    /**
     * 根据Id批量删除采购需求
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/{ids}")
    public R delete(@PathVariable("ids") Long[] ids){
        purchaseDetailService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
