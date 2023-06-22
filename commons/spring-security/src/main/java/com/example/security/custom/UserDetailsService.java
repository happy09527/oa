package com.example.security.custom;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author: ZhangX
 * @createDate: 2023/6/16
 * @description:
 */

public interface UserDetailsService {
    /**
     * 根据用户名获取用户对象（获取不到直接抛异常）
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
