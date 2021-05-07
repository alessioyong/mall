package com.yxx.mall.common.entity.backend;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 通知公告表
 * 
 * @author xyong
 * @date 2021-05-07 10:42:50
 */
@Data
@TableName("sys_notice")
public class SysNoticeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 公告ID
	 */
	@TableId
	private Integer noticeId;
	/**
	 * 公告标题
	 */
	private String noticeTitle;
	/**
	 * 公告类型（1通知 2公告）
	 */
	private String noticeType;
	/**
	 * 公告内容
	 */
	private byte noticeContent;
	/**
	 * 公告状态（0正常 1关闭）
	 */
	private String status;
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
