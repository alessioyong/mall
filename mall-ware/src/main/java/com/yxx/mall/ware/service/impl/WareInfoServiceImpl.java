package com.yxx.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.ware.WareInfoEntity;
import com.yxx.mall.common.utils.StringUtils;
import com.yxx.mall.ware.mapper.WareInfoMapper;
import com.yxx.mall.ware.service.WareInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author xyong
 * date 2021-06-03
 */
@Service
public class WareInfoServiceImpl extends ServiceImpl<WareInfoMapper, WareInfoEntity> implements WareInfoService {
    @Override
    public PageInfo listWareInfo(Map<String, Object> params) {
        int pageNum = Integer.parseInt((String) params.get("pageNum"));
        int pageSize = Integer.parseInt((String) params.get("pageSize"));
        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        String name = (String) params.get("name");
        if(StringUtils.isNotEmpty(name)){
            wrapper.like("name",name);
        }
        List<WareInfoEntity> list = this.list(wrapper);
        PageHelper.startPage(pageNum,pageSize);
        PageInfo page=new PageInfo(list);
        return page;
    }
}
