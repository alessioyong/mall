package com.yxx.mall.product.vo;

import com.yxx.mall.common.entity.product.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-27
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
