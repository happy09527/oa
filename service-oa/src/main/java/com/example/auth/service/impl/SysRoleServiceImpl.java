package com.example.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.mapper.SysRoleMapper;
import com.example.auth.service.SysRoleService;
import com.example.auth.service.SysUserRoleService;
import com.example.model.system.SysRole;
import com.example.model.system.SysUser;
import com.example.model.system.SysUserRole;
import com.example.vo.system.AssignRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: ZhangX
 * @createDate: 2023/5/20
 * @description:
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {


    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public void doAssign(AssignRoleVo assignRoleVo) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,assignRoleVo.getUserId());
        sysUserRoleService.remove(queryWrapper);
        if(assignRoleVo.getRoleIdList().size()==0){
            return;
        }
        List<SysUserRole> sysUserRoles = new ArrayList<>();
        for(Long roleId : assignRoleVo.getRoleIdList()){
            sysUserRoles.add(new SysUserRole(roleId,assignRoleVo.getUserId()));
        }
        sysUserRoleService.saveBatch(sysUserRoles);
    }

    /**
     * @author: ZhangX
     * @date: 2023/6/10 10:43
     * @param: [userId]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     * @description: 查询用户的所有角色
     **/
    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {

        List<SysRole> sysRoles = baseMapper.selectList(null);
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserRole::getUserId,userId);
        List<SysUserRole> sysUserRoles = sysUserRoleService.list(queryWrapper);
        List<Long> sysUserRoleId = sysUserRoles.stream().map(c->c.getRoleId()).collect(Collectors.toList());
        Map<String , Object> sysRoleMap = new HashMap<>();
        List<SysRole> assignSysRoles = new ArrayList<>();
        for(SysRole sysRole : sysRoles){
            if(sysUserRoleId.contains(sysRole.getId())){
                assignSysRoles.add(sysRole);
            }
        }
        sysRoleMap.put("assignSysRoles",assignSysRoles);
        sysRoleMap.put("allSysRoles",sysRoles);
        return sysRoleMap;
    }
}
