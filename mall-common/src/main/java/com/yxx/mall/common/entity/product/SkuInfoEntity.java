package com.yxx.mall.common.entity.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * sku信息
 * 
 * @author xyong
 * @date 2021-05-07 10:53:45
 */
@Data
@TableName("pms_sku_info")
public class SkuInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * skuId
	 */
	@TableId
	private Long skuId;
	/**
	 * spuId
	 */
	private Long spuId;
	/**
	 * sku名称
	 */
	private String skuName;
	/**
	 * sku介绍描述
	 */
	private String skuDesc;
	/**
	 * 所属分类id
	 */
	private Long catalogId;
	/**
	 * 品牌id
	 */
	private Long brandId;
	/**
	 * 默认图片
	 */
	private String skuDefaultImg;
	/**
	 * 标题
	 */
	private String skuTitle;
	/**
	 * 副标题
	 */
	private String skuSubtitle;
	/**
	 * 价格
	 */
	private BigDecimal price;
	/**
	 * 销量
	 */
	private Long saleCount;

}
