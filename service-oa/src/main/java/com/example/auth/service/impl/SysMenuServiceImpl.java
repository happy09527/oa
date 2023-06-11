package com.example.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.auth.mapper.SysMenuMapper;
import com.example.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.service.SysRoleMenuService;
import com.example.auth.utils.SysMenuUtil;
import com.example.common.config.exception.MyException;
import com.example.model.system.SysMenu;
import com.example.model.system.SysRole;
import com.example.model.system.SysRoleMenu;
import com.example.vo.system.AssignMenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author Zhangx
 * @since 2023-06-11
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenus = baseMapper.selectList(null);
        List<SysMenu> resultData = SysMenuUtil.buildTree(sysMenus);
        return resultData;
    }

    @Override
    public void removeMenuById(Long id) {
        // 判断当前菜单是否有下一层菜单
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysMenu::getParentId, id);

        Integer count = baseMapper.selectCount(lambdaQueryWrapper);

        if (count > 0) {
            throw new MyException(201, "菜单不能删除");
        }
        baseMapper.deleteById(id);
    }

    @Override
    public List<SysMenu> findMenuByRoleId(Long roleId) {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getStatus, 1);
        List<SysMenu> sysMenus = baseMapper.selectList(queryWrapper);

        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.eq(SysRoleMenu::getRoleId, roleId);
        List<SysRoleMenu> sysRoleMenus = sysRoleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        List<Long> menuIds = sysRoleMenus.stream().map(c -> c.getMenuId()).collect(Collectors.toList());
        for (SysMenu sysMenu : sysMenus) {
            if (menuIds.contains(sysMenu.getId())) {
                sysMenu.setSelect(true);
            } else {
                sysMenu.setSelect(false);
            }
        }
        List<SysMenu> sysMenuList = SysMenuUtil.buildTree(sysMenus);
        return sysMenuList;
    }

    @Override
    public void doAssign(AssignMenuVo assignMenuVo) {
        // 删除原有菜单
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId());
        sysRoleMenuService.remove(queryWrapper);

        // 新增菜单
        if (assignMenuVo.getMenuIdList().size() == 0) {
            return;
        }
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        for(Long menuId : assignMenuVo.getMenuIdList()){
            sysRoleMenus.add(new SysRoleMenu(assignMenuVo.getRoleId(),menuId));
        }
        sysRoleMenuService.saveBatch(sysRoleMenus);
    }
}
