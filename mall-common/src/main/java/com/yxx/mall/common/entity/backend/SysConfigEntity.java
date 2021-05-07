package com.yxx.mall.common.entity.backend;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 参数配置表
 * 
 * @author xyong
 * @date 2021-05-07 10:42:50
 */
@Data
@TableName("sys_config")
public class SysConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 参数主键
	 */
	@TableId
	private Integer configId;
	/**
	 * 参数名称
	 */
	private String configName;
	/**
	 * 参数键名
	 */
	private String configKey;
	/**
	 * 参数键值
	 */
	private String configValue;
	/**
	 * 系统内置（Y是 N否）
	 */
	private String configType;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新者
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 备注
	 */
	private String remark;

}
