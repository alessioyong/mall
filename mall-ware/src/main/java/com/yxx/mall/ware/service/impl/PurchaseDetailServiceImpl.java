package com.yxx.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.PurchaseDetailEntity;
import com.yxx.mall.common.utils.StringUtils;
import com.yxx.mall.ware.mapper.PurchaseDetailMapper;
import com.yxx.mall.ware.service.PurchaseDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xyong
 * date 2021-06-24
 */
@Service
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailMapper, PurchaseDetailEntity> implements PurchaseDetailService {
    @Override
    public PageInfo listByConditions(Map<String, Object> parmas) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        Integer pageNum = Integer.valueOf((String) parmas.get("pageNum"));
        Integer pageSize = Integer.valueOf((String) parmas.get("pageSize"));
        String key = (String) parmas.get("key");
        if(StringUtils.isNotEmpty(key)){
            wrapper.and(w->{
                w.eq("purchase_id",key).or().eq("sku_id",key);
            });
        }
        String status = (String) parmas.get("status");
        if(StringUtils.isNotEmpty(status)){
            wrapper.eq("status",status);
        }
        String wareId = (String) parmas.get("wareId");
        if(StringUtils.isNotEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }
        PageHelper.startPage(pageNum,pageSize);
        List<PurchaseDetailEntity> list = this.list(wrapper);
        return new PageInfo(list);
    }
}
