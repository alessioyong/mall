package com.yxx.mall.common.entity.coupon;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 优惠券分类关联对象 sms_coupon_spu_category_relation
 * 
 * @author xyong
 * @date 2021-05-25
 */
@Data
@TableName("sms_coupon_spu_category_relation")
public class CouponSpuCategoryRelationEntity {
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId
    private Long id;

    /** 优惠券id */

    private Long couponId;

    /** 产品分类id */

    private Long categoryId;

    /** 产品分类名称 */

    private String categoryName;

}
