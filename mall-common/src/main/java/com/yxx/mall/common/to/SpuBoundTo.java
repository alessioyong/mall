package com.yxx.mall.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xyong
 * date 2021-05-28
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
