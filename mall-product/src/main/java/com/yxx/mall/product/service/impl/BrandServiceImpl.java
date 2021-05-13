package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.BrandEntity;
import com.yxx.mall.product.mapper.BrandMapper;
import com.yxx.mall.product.service.BrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author xyong
 * date 2021-05-13
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, BrandEntity> implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    /**
     * 查询品牌列表
     * @param brand
     * @return
     */
    @Override
    public List<BrandEntity> listByBrand(BrandEntity brand) {
        return brandMapper.selectBrandList(brand);
    }

    /**
     * 批量删除品牌
     * @param brandIds
     */
    @Override
    public void batchRemove(Long[] brandIds) {
        //TODO 判断品牌是否被依赖
        baseMapper.deleteBatchIds(Arrays.asList(brandIds));
    }
}
