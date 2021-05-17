package com.yxx.mall.common.entity.product;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yxx.mall.common.validated.AddGroup;
import com.yxx.mall.common.validated.EditGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * 品牌
 * 
 * @author xyong
 * @email ${email}
 * @date 2021-05-12 10:31:45
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@Null(message ="新增时不能指定品牌ID",groups = {AddGroup.class})
	@NotNull(message = "品牌ID不能为空",groups = {EditGroup.class})
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(message = "品牌名不能为空",groups = {AddGroup.class,EditGroup.class})
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(message = "品牌logo地址不能为空",groups = {AddGroup.class})
	@URL(message = "品牌logo地址必须是一个合法的地址",groups = {AddGroup.class,EditGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	@NotBlank(message = "品牌介绍不能为空",groups = {AddGroup.class})
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@NotNull(message = "品牌状态不能为空",groups = {AddGroup.class,EditGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(message = "检索首字母不能为空",groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "检索首字母必须是一个a-z或A-Z的英文字母",groups = {AddGroup.class,EditGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@NotNull(message = "排序字段不能为空",groups = {AddGroup.class})
	@Min(value = 0,message = "排序字段必须是一个大于等于0的整数",groups = {AddGroup.class,EditGroup.class})
	private Integer sort;

}
