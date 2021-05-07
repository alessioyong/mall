package com.yxx.mall.common.entity.backend;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统访问记录
 * 
 * @author xyong
 * @date 2021-05-07 10:42:50
 */
@Data
@TableName("sys_logininfor")
public class SysLogininforEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 访问ID
	 */
	@TableId
	private Long infoId;
	/**
	 * 用户账号
	 */
	private String userName;
	/**
	 * 登录IP地址
	 */
	private String ipaddr;
	/**
	 * 登录状态（0成功 1失败）
	 */
	private String status;
	/**
	 * 提示信息
	 */
	private String msg;
	/**
	 * 访问时间
	 */
	private Date accessTime;

}
