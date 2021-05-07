package com.yxx.mall.common.entity.backend;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 定时任务调度日志表
 * 
 * @author xyong
 * @date 2021-05-07 10:42:50
 */
@Data
@TableName("sys_job_log")
public class SysJobLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务日志ID
	 */
	@TableId
	private Long jobLogId;
	/**
	 * 任务名称
	 */
	private String jobName;
	/**
	 * 任务组名
	 */
	private String jobGroup;
	/**
	 * 调用目标字符串
	 */
	private String invokeTarget;
	/**
	 * 日志信息
	 */
	private String jobMessage;
	/**
	 * 执行状态（0正常 1失败）
	 */
	private String status;
	/**
	 * 异常信息
	 */
	private String exceptionInfo;
	/**
	 * 创建时间
	 */
	private Date createTime;

}
