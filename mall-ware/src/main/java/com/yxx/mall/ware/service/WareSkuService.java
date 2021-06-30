package com.yxx.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.WareSkuEntity;
import com.yxx.mall.ware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * @author xyong
 * date 2021-06-03
 */
public interface WareSkuService extends IService<WareSkuEntity> {
    PageInfo getWareSkuList(Map<String, Object> params);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}
