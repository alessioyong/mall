package com.yxx.mall.common.entity.backend;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色和部门关联表
 * 
 * @author xyong
 * @date 2021-05-07 10:42:50
 */
@Data
@TableName("sys_role_dept")
public class SysRoleDeptEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@TableId
	private Long roleId;
	/**
	 * 部门ID
	 */
	private Long deptId;

}
