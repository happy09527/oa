package com.example.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.system.SysRole;
import com.example.vo.system.AssignRoleVo;
import com.example.vo.system.RouterVo;

import java.util.List;
import java.util.Map;

/**
 * @author: ZhangX
 * @createDate: 2023/5/20
 * @description:
 */

public interface SysRoleService extends IService<SysRole> {
    void doAssign(AssignRoleVo assignRoleVo);

    Map<String, Object> findRoleDataByUserId(Long userId);
}
