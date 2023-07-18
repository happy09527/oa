package com.example.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.auth.mapper.SysUserMapper;
import com.example.auth.service.SysMenuService;
import com.example.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.JWTUtil;
import com.example.common.config.exception.MyException;
import com.example.common.utils.MD5Util;
import com.example.model.system.SysUser;
import com.example.security.custom.LoginUserInfoHelper;
import com.example.vo.system.LoginVo;
import com.example.vo.system.RouterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Zhangx
 * @since 2023-06-01
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public Map<String, Object> getCurrentUser() {
        SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());
        System.out.println(map);
        return map;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser sysUser = baseMapper.selectById(id);
        // 设置修改状态
        if (status == 0 || status == 1) {
            sysUser.setStatus(status);
        } else {
            log.info("数值不合法");
        }
        baseMapper.updateById(sysUser);
    }

    @Override
    public String login(LoginVo loginVo) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, loginVo.getUsername());
        SysUser sysUser = baseMapper.selectOne(queryWrapper);
        if (sysUser == null) {
            throw new MyException(202, "用户不存在");
        }
        String password = sysUser.getPassword();
        if (!password.equals(MD5Util.encrypt(loginVo.getPassword()))) {
            throw new MyException(202, "用户密码错误");
        }
        String token = JWTUtil.createToken(sysUser.getId(), sysUser.getUsername());
        return token;
    }

    @Override
    public boolean saveUser(SysUser user) {
        String password = MD5Util.encrypt(user.getPassword());
        user.setPassword(password);
        int isSuccess = baseMapper.insert(user);
        if (isSuccess == 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Map<String, Object> getInfo(String token) {
        Long userId = JWTUtil.getUserId(token);
        SysUser sysUser = baseMapper.selectById(userId);
        log.info(sysUser.toString());
        Map<String, Object> map = new HashMap<>();
        List<RouterVo> menuList = sysMenuService.findUserMenuByUserId(userId);
        List<String> permissionList = sysMenuService.findUserPermissionsByUserId(userId);
        map.put("roles", new HashSet<>());
        map.put("name", sysUser.getName());
        map.put("avatar", sysUser.getHeadUrl());
        map.put("routers", menuList);
        map.put("buttons", permissionList);
        return map;
    }

    // 根据用户名查询
    @Override
    public SysUser getUserByUserName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = baseMapper.selectOne(queryWrapper);
        return sysUser;
    }
}
