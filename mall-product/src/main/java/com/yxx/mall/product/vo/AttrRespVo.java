package com.yxx.mall.product.vo;

import lombok.Data;

/**
 * @author xyong
 * date 2021-05-21
 */
@Data
public class AttrRespVo extends AttrVo{

    /**
     * 分类名字
     */
    private String catelogName;

    /**
     * 分组名字
     */
    private String groupName;

    /**
     * 分组完整路径
     */
    private Long[] catelogPath;
}
