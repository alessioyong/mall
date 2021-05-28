package com.yxx.mall.common.entity.coupon;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品阶梯价格对象 sms_sku_ladder
 * 
 * @author xyong
 * @date 2021-05-25
 */
@Data
@TableName("sms_sku_ladder")
public class SkuLadderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id */
    @TableId
    private Long id;

    /** spu_id */
    private Long skuId;

    /** 满几件 */
    private Integer fullCount;

    /** 打几折 */
    private BigDecimal discount;

    /** 折后价 */
    private BigDecimal price;

    /** 是否叠加其他优惠[0-不可叠加，1-可叠加] */
    private Integer addOther;


}
