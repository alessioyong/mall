package com.yxx.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.constant.ProductConstant;
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
import com.yxx.mall.product.vo.AttrGroupRelationVo;
import com.yxx.mall.product.vo.AttrRespVo;
import com.yxx.mall.product.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xyong
 * date 2021-05-20
 */
@Service
@Slf4j
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
     * @param type
     * @return
     */
    @Override
    public List<AttrRespVo> selectAttrList(AttrEntity attr, String type) {
        if(type.equals("base")){
            attr.setAttrType(ProductConstant.AttrTypeEnum.ATTR_BASE_TYPE.getCode());
        }else {
            attr.setAttrType(ProductConstant.AttrTypeEnum.ATTR_SALE_TYPE.getCode());
        }
        List<AttrEntity> attrEntities = baseMapper.selectAttrList(attr);
        List<AttrRespVo> attrRespVos = attrEntities.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //1.设置分类和分组的名字
            if("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity relationEntity = relationMapper.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId()));
                if (relationEntity != null&&relationEntity.getAttrGroupId()!=null) {
                    AttrGroupEntity groupEntity = attrGroupMapper.selectById(relationEntity.getAttrGroupId());
                    attrRespVo.setGroupName(groupEntity.getAttrGroupName());
                }
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
        if(attrVo.getAttrType()==ProductConstant.AttrTypeEnum.ATTR_BASE_TYPE.getCode()&&attrVo.getAttrGroupId()!=null){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationMapper.insert(relationEntity);
        }

    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrRespVo attrRespVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrRespVo);
        if(attrEntity.getAttrType()==ProductConstant.AttrTypeEnum.ATTR_BASE_TYPE.getCode()){
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
        if(attr.getAttrType()==ProductConstant.AttrTypeEnum.ATTR_BASE_TYPE.getCode()){
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

    /**
     * 根据分组ID查询关联的所有基本属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationMapper.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_group_id", attrgroupId));
        log.info("relation:{}",relationEntities);
        if(relationEntities!=null){
            List<Long> attrIds = relationEntities.stream().map((attr) -> {
                return attr.getAttrId();
            }).collect(Collectors.toList());
            log.info("ids:{}",attrIds);
            if(CollectionUtils.isEmpty(attrIds)){
                return null;
            }
            List<AttrEntity> entities = this.listByIds(attrIds);
            return entities;
        }
        return null;
    }

    /**
     * 删除属性分组关联关系
     * @param vos
     */
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        List<AttrAttrgroupRelationEntity> entities = Arrays.asList(vos).stream().map((item) -> {
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, relationEntity);
            return relationEntity;
        }).collect(Collectors.toList());
        relationMapper.deleteBathRelation(entities);
    }

    /**
     * 获取当前分组没有关联的所有属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageInfo getNoRelationAttr(Map<String, Object> params, Long attrgroupId) {
        //1.当前分组只能关联自己所属分类中的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupMapper.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2.当前分组只能关联别的分组没有引用的属性
        //2.1当前分类下的其他属性
        List<AttrGroupEntity> group = attrGroupMapper.selectList(new QueryWrapper<AttrGroupEntity>()
                .eq("catelog_id", catelogId));
        List<Long> collect = group.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //2.2这些分组关联的属性
        List<AttrAttrgroupRelationEntity> relationEntities = relationMapper.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .in("attr_group_id", collect));
        List<Long> attrIds = relationEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        //2.3从当前分类的所有属性中移除这些属性
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId).eq("attr_type",ProductConstant.AttrTypeEnum.ATTR_BASE_TYPE.getCode());
        if(attrIds!=null&&attrIds.size()>0){
              wrapper.notIn("attr_id", attrIds);
        }
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.eq("attr_id",key).or().like("attr_name",key);
        }
        List<AttrEntity> attrEntities = this.baseMapper.selectList(wrapper);
        Integer pageNum = Integer.valueOf((String) params.get("pageNum")) ;
        Integer pageSize =Integer.valueOf((String) params.get("pageSize")) ;
        PageHelper.startPage(pageNum,pageSize);
        PageInfo page=new PageInfo(attrEntities);

        return page;
    }


    /**
     * 根据分类ID查询销售属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrEntity> getSaleAttrByCatelogId(Long catelogId) {
        List<AttrEntity> attrEntities = this.list(new QueryWrapper<AttrEntity>()
                .eq("catelog_id", catelogId)
                .eq("attr_type", ProductConstant.AttrTypeEnum.ATTR_SALE_TYPE.getCode()));
        return attrEntities;
    }

    /**
     * 在指定的所有属性中可搜索的
     * @param attrIds
     * @return
     */
    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {
        return baseMapper.selectSearchAttrIds(attrIds);
    }
}
