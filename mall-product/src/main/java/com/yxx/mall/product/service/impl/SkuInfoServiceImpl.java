package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.SkuInfoEntity;
import com.yxx.mall.common.utils.StringUtils;
import com.yxx.mall.product.mapper.SkuInfoMapper;
import com.yxx.mall.product.service.SkuInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xyong
 * date 2021-05-27
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfoEntity> implements SkuInfoService {
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageInfo getSkuInfoByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        PageInfo page=null;
        String key = (String) params.get("key");
        if(StringUtils.isNotEmpty(key)){
            wrapper.and((w)->{
                w.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if(StringUtils.isNotEmpty(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        String brandId = (String) params.get("brandId");
        if(StringUtils.isNotEmpty(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String min = (String) params.get("min");
        if(StringUtils.isNotEmpty(min)){
            wrapper.ge("price",min);
        }
        String max = (String) params.get("max");
        if(StringUtils.isNotEmpty(max)){
            wrapper.le("price",max);
        }
        Integer pageNum = Integer.parseInt((String) params.get("pageNum"));
        Integer pageSize = Integer.parseInt((String) params.get("pageSize"));
        if(pageNum!=null&&pageSize!=null){
            PageHelper.startPage(pageNum,pageSize);
            List<SkuInfoEntity> list = this.list(wrapper);
            page=new PageInfo(list);
        }
        return page;
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {

        return this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id",spuId));
    }
}
