
package com.yxx.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xyong
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}