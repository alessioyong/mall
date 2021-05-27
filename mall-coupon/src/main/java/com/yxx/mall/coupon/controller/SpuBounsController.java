package com.yxx.mall.coupon.controller;

import com.yxx.mall.common.entity.coupon.SpuBoundsEntity;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.coupon.service.SpuBoundsService;
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
@RequestMapping("/coupon/bounds")
public class SpuBounsController {
    @Autowired
    SpuBoundsService boundsService;

    @PostMapping("/save")
    public R save(@RequestBody SpuBoundsEntity boundsEntity){
        boundsService.save(boundsEntity);
        return R.ok();
    }

}
