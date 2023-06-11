package com.example.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.system.SysMenu;
import com.example.vo.system.AssignMenuVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author Zhangx
 * @since 2023-06-11
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> findNodes();

    void removeMenuById(Long id);

    List<SysMenu> findMenuByRoleId(Long roleId);

    void doAssign(AssignMenuVo assignMenuVo);
}
