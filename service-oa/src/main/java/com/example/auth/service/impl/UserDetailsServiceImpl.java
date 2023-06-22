package com.example.auth.service.impl;

import com.example.auth.service.SysMenuService;
import com.example.auth.service.SysUserService;
import com.example.model.system.SysUser;
import com.example.security.custom.CustomUser;
//import com.example.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: ZhangX
 * @createDate: 2023/6/16
 * @description:
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 根据用户名查询
        SysUser sysUser = sysUserService.getUserByUserName(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }

        // 根据 user_id 查询用户操作权限数据
        List<String> userPermsList = sysMenuService.findUserPermissionsByUserId(sysUser.getId());
        // 创建list集合，封装最终权限数据
        List<SimpleGrantedAuthority> authList =  new ArrayList<>();
        // 遍历 authList
        for (String perms : userPermsList) {
            authList.add(new SimpleGrantedAuthority(perms.trim()));
        }

        return new CustomUser(sysUser, authList);
    }
}
