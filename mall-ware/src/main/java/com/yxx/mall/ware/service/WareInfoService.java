package com.yxx.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.WareInfoEntity;

import java.util.Map;

/**
 * @author xyong
 * date 2021-06-03
 */
public interface WareInfoService extends IService<WareInfoEntity> {
    PageInfo listWareInfo(Map<String, Object> params);
}
