package com.yxx.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.yxx.mall.common.entity.product.AttrEntity;
import com.yxx.mall.product.vo.AttrGroupRelationVo;
import com.yxx.mall.product.vo.AttrRespVo;
import com.yxx.mall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

public interface AttrService extends IService<AttrEntity> {
    List<AttrRespVo> selectAttrList(AttrEntity attr, String type);

    void saveAttr(AttrVo attrVo);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    void deleteAttr(List<Long> ids);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageInfo getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
}
