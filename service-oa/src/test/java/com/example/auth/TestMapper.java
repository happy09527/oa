package com.example.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.auth.mapper.SysRoleMapper;
import com.example.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author: ZhangX
 * @createDate: 2023/5/19
 * @description:
 */

@SpringBootTest
public class TestMapper {
    @Autowired
    SysRoleMapper sysRoleMapper;
    @Test
    public void test1(){
        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        System.out.println(sysRoles);
    }
    @Test
    public void test2(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("zx");
        sysRoleMapper.insert(sysRole);
    }
    @Test
    public void test3(){
        SysRole sysRole = new SysRole();
        sysRole.setId(9L);
        sysRole.setRoleName("zx");
        sysRole.setIsDeleted(1);
        System.out.println(sysRole.getId());
//        sysRoleMapper.updateById(sysRole);
    }
}
