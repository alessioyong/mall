package com.yxx.mall.common.entity.coupon;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品满减信息对象 sms_sku_full_reduction
 * 
 * @author xyong
 * @date 2021-05-25
 */
@Data
@TableName("sms_sku_full_reduction")
public class SkuFullReductionEntity {
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId
    private Long id;

    /** spu_id */
    private Long skuId;

    /** 满多少 */
    private BigDecimal fullPrice;

    /** 减多少 */
    private BigDecimal reducePrice;

    /** 是否参与其他优惠 */
    private Integer addOther;


}
