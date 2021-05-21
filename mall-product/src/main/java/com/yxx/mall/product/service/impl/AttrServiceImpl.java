package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yxx.mall.common.entity.product.AttrAttrgroupRelationEntity;
import com.yxx.mall.common.entity.product.AttrEntity;
import com.yxx.mall.common.entity.product.AttrGroupEntity;
import com.yxx.mall.common.entity.product.CategoryEntity;
import com.yxx.mall.product.mapper.AttrAttrgroupRelationMapper;
import com.yxx.mall.product.mapper.AttrGroupMapper;
import com.yxx.mall.product.mapper.AttrMapper;
import com.yxx.mall.product.mapper.CategoryMapper;
import com.yxx.mall.product.service.AttrService;
import com.yxx.mall.product.vo.AttrRespVo;
import com.yxx.mall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xyong
 * date 2021-05-20
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, AttrEntity> implements AttrService {

    @Resource
    AttrAttrgroupRelationMapper relationMapper;

    @Resource
    AttrGroupMapper attrGroupMapper;

    @Resource
    CategoryMapper categoryMapper;

    /**
     * 查询属性，规格参数列表
     * @param attr
     * @return
     */
    @Override
    public List<AttrRespVo> selectAttrList(AttrEntity attr) {
        List<AttrEntity> attrEntities = baseMapper.selectAttrList(attr);
        List<AttrRespVo> attrRespVos = attrEntities.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //1.设置分类和分组的名字
            AttrAttrgroupRelationEntity relationEntity = relationMapper.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attrEntity.getAttrId()));
            if (relationEntity != null) {
                AttrGroupEntity groupEntity = attrGroupMapper.selectById(relationEntity.getAttrGroupId());
                attrRespVo.setGroupName(groupEntity.getAttrGroupName());
            }
            CategoryEntity categoryEntity = categoryMapper.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            return attrRespVo;
        }).collect(Collectors.toList());
        return attrRespVos;
    }

    /**
     * 保存规格参数信息
     * @param attrVo
     */
    @Override
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo,attrEntity);
        //1.保存基本数据
        this.save(attrEntity);
        //2.保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationMapper.insert(relationEntity);
    }
}
