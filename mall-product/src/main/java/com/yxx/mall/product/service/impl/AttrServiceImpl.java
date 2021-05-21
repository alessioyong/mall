package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.yxx.mall.product.service.CategoryService;
import com.yxx.mall.product.vo.AttrRespVo;
import com.yxx.mall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    CategoryService categoryService;

    /**
     * 查询属性，规格参数列表
     *
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
     *
     * @param attrVo
     */
    @Override
    @Transactional
    public void saveAttr(AttrVo attrVo) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attrVo, attrEntity);
        //1.保存基本数据
        this.save(attrEntity);
        //2.保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationMapper.insert(relationEntity);
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        //设置分组信息
        AttrAttrgroupRelationEntity relationEntity = relationMapper.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrEntity.getAttrId()));
        if (relationEntity != null) {
            attrRespVo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupMapper.selectById(relationEntity.getAttrGroupId());
            if (attrGroupEntity != null) {
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        //设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrRespVo.setCatelogPath(catelogPath);
        return attrRespVo;
    }

    /**
     * 跟新规格参数信息
     *
     * @param attr
     */
    @Override
    @Transactional
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);
        //更改关联表中的值
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attr.getAttrId());
        relationEntity.setAttrGroupId(attr.getAttrGroupId());

        Integer count = relationMapper.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attr.getAttrId()));
        if (count > 0) {
            relationMapper.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attr.getAttrId()));
        } else {
            relationMapper.insert(relationEntity);
        }
    }

    /**
     * 批量删除规格参数
     *
     * @param ids
     */
    @Override
    public void deleteAttr(List<Long> ids) {
        if (ids != null) {
            this.removeByIds(ids);
            //再删除关联表中信息
            for (Long id : ids) {
                relationMapper.delete(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id",id));
            }
        }

    }
}
