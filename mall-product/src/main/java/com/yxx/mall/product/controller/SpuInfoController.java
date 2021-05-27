package com.yxx.mall.product.controller;

import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.SpuInfoService;
import com.yxx.mall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xyong
 * date 2021-05-27
 */
@RestController
@RequestMapping("/product/spuinfo")
public class SpuInfoController {

    @Autowired
    SpuInfoService spuInfoService;


    @PostMapping("/save")
    public R save(@RequestBody SpuSaveVo vo){
        spuInfoService.saveSpuInfo(vo);
        return R.ok();
    }

}
