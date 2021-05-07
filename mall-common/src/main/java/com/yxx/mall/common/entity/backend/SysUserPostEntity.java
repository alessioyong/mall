package com.yxx.mall.common.entity.backend;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户与岗位关联表
 * 
 * @author xyong
 * @date 2021-05-07 10:42:50
 */
@Data
@TableName("sys_user_post")
public class SysUserPostEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId
	private Long userId;
	/**
	 * 岗位ID
	 */
	private Long postId;

}
