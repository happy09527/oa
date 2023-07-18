package com.example.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.auth.mapper.SysRoleMapper;
import com.example.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
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
//        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
//        System.out.println(sysRoles);
        try{
            int i = 10/ 0;
        }catch (Exception e){
            throw new RuntimeException();
        }finally {
            System.out.println("aaa");
        }
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
        sysRole.setCreateTime(new Date());
        System.out.println(sysRole.getId());
//        sysRoleMapper.updateById(sysRole);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleName,"系统管理员");
        List<SysRole> sysRoles = sysRoleMapper.selectList(wrapper);
        System.out.println(sysRoles);
    }
    @Test
    public void test4(){
        SysRole sysRole = new SysRole();
        sysRole.setRoleName("zzz");
        sysRole.setCreateTime(new Date());
        LambdaUpdateWrapper<SysRole> wrapper1 =new LambdaUpdateWrapper<>();
        wrapper1.eq(SysRole::getRoleName,"zx");
        sysRoleMapper.update(sysRole,wrapper1);
    }


}
