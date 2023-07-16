package com.example.security.filter;


import com.alibaba.fastjson2.JSON;
import com.example.common.result.Result;
import com.example.common.result.ResultCodeEnum;
import com.example.common.utils.JWTUtil;
import com.example.common.utils.ResponseUtil;
import com.example.security.custom.LoginUserInfoHelper;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: ZhangX
 * @createDate: 2023/6/22
 * @description:
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        logger.info("uri:" + httpServletRequest.getRequestURI());
        //如果是登录接口，直接放行
        if ("/admin/system/index/login".equals(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(httpServletRequest);
        if (null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            ResponseUtil.out(httpServletResponse, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:" + token);
        if (!StringUtils.isEmpty(token)) {
            String username = JWTUtil.getUsername(token);
            logger.info("username:" + username);
            if (!StringUtils.isEmpty(username)) {
                //通过ThreadLocal记录当前登录人信息
                LoginUserInfoHelper.setUserId(JWTUtil.getUserId(token));
                LoginUserInfoHelper.setUsername(username);
                String authoritiesString = (String) redisTemplate.opsForValue().get(username);
                List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                for (Map map : mapList) {
                    authorities.add(new SimpleGrantedAuthority((String) map.get("authority")));
                }
                return new UsernamePasswordAuthenticationToken(username, null, authorities);
            } else {
                return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
            }
        }
        return null;
    }
}
