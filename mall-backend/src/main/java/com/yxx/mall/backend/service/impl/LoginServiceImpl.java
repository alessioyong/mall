package com.yxx.mall.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxx.mall.backend.model.LoginUser;
import com.yxx.mall.backend.service.LoginService;
import com.yxx.mall.backend.service.PermissionService;
import com.yxx.mall.backend.service.SysUserService;
import com.yxx.mall.common.entity.backend.SysUserEntity;
import com.yxx.mall.common.utils.RRException;
import com.yxx.mall.common.utils.constant.Constants;
import com.yxx.mall.common.utils.constant.UserStatus;
import com.yxx.mall.common.utils.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author xyong
 * date 2021-05-07
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    RedisService redisService;

    @Autowired
    private SysUserService userService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public LoginUser login(String username, String password, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        //从redis中取出验证码
        String captcha = redisService.getCacheObject(verifyKey);
        //删除验证码
        redisService.deleteObject(verifyKey);
        //验证验证码
        if(captcha==null){
            throw new RRException("验证码失效，请刷新后重新输入！");
        }
        if(!code.equalsIgnoreCase(captcha)){
            throw new RRException("验证码错误！");
        }
        //用户验证
        SysUserEntity userInfo = userService.getOne(new QueryWrapper<SysUserEntity>().eq("user_name", username));
        if(userInfo==null){
            throw new RRException("登录用户："+username+" 不存在");
        }
        if(userInfo.getStatus().equals(UserStatus.DISABLE.getValue())){
            throw new RRException("登录用户："+username+" 已被禁用，请联系管理员！");
        }
        if(!SecurityUtils.matchesPassword(password,userInfo.getPassword())){
            throw new RRException("密码错误，请重新输入！");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(userInfo);
        //查询用户角色
        Set<String> roles = permissionService.getRolePermission(userInfo.getUserId());
        //查询用户权限
        Set<String> permissions = permissionService.getMenuPermission(userInfo.getUserId());
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        return loginUser;
    }
}
