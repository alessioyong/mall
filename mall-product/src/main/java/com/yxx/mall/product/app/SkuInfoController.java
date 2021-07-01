package com.yxx.mall.product.app;

import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xyong
 * date 2021-06-02
 */
@RestController
@RequestMapping("/product/skuinfo")
public class SkuInfoController {

    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/list")
    public R list(@RequestParam Map<String,Object> params){
        PageInfo page=skuInfoService.getSkuInfoByCondition(params);
        return R.ok().put("data",page);
    }
}
