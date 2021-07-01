package com.yxx.mall.product.app;

import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.utils.R;
import com.yxx.mall.product.service.SpuInfoService;
import com.yxx.mall.product.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author xyong
 * date 2021-05-27
 */
@RestController
@RequestMapping("/product/spuinfo")
public class SpuInfoController {

    @Autowired
    SpuInfoService spuInfoService;


    /**
     * 新增商品信息
     * @param vo
     * @return
     */
    @PostMapping("/save")
    public R save(@RequestBody SpuSaveVo vo){
        spuInfoService.saveSpuInfo(vo);
        return R.ok();
    }


    /**
     * 条件查询出商品信息
     * @param params
     * @return
     */
    @GetMapping("/list")
    public R getSpuInfo(@RequestParam Map<String,Object> params){
        PageInfo page=spuInfoService.getSpuInfoByCondition(params);
        return R.ok().put("data",page);
    }

    /**
     * 商品上架
     * @param id
     * @return
     */
    @PutMapping("/up/{id}")
    public R upSpu(@PathVariable("id") Long id){
        /*SpuInfoEntity spuInfo = new SpuInfoEntity();
        spuInfo.setId(id);
        spuInfo.setPublishStatus(ProductConstant.PublishStatusEnum.PUBLISH_UP.getCode());
        spuInfoService.updateById(spuInfo);*/
        spuInfoService.up(id);
        return R.ok();
    }
}
