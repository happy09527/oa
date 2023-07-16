package com.example.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.system.SysUser;
import com.example.vo.system.LoginVo;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author Zhangx
 * @since 2023-06-01
 */
public interface SysUserService extends IService<SysUser> {

    void updateStatus(Long id, Integer status);

    String login(LoginVo loginVo);

    boolean saveUser(SysUser user);

    Map<String, Object> getInfo(String token);

    SysUser getUserByUserName(String username);

    Map<String, Object> getCurrentUser();
}
