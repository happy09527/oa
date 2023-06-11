package com.example.auth.service.impl;

import com.example.auth.mapper.SysUserMapper;
import com.example.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.model.system.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
