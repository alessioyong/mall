package com.yxx.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.WareSkuEntity;
import com.yxx.mall.common.utils.StringUtils;
import com.yxx.mall.ware.mapper.WareSkuMapper;
import com.yxx.mall.ware.service.WareSkuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xyong
 * date 2021-06-03
 */
@Service
public class WareSkuServiceImpl extends ServiceImpl<WareSkuMapper, WareSkuEntity> implements WareSkuService {
    @Override
    public PageInfo getWareSkuList(Map<String, Object> params) {
        int pageNum = Integer.parseInt((String) params.get("pageNum"));
        int pageSize = Integer.parseInt((String) params.get("pageSize"));

        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(StringUtils.isNotEmpty(skuId)){
            wrapper.eq("sku_id",skuId);
        }
        String wareId = (String) params.get("wareId");
        if(StringUtils.isNotEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }
        PageHelper.startPage(pageNum,pageSize);
        List<WareSkuEntity> list = this.list(wrapper);
        return new PageInfo(list);
    }
}
