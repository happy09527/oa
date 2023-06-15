package com.example.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.auth.mapper.SysMenuMapper;
import com.example.auth.service.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.auth.service.SysRoleMenuService;
import com.example.auth.utils.SysMenuUtil;
import com.example.common.config.exception.MyException;
import com.example.model.system.SysMenu;
import com.example.model.system.SysRoleMenu;
import com.example.vo.system.AssignMenuVo;
import com.example.vo.system.MetaVo;
import com.example.vo.system.RouterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
        for (Long menuId : assignMenuVo.getMenuIdList()) {
            sysRoleMenus.add(new SysRoleMenu(assignMenuVo.getRoleId(), menuId));
        }
        sysRoleMenuService.saveBatch(sysRoleMenus);
    }

    @Override
    public List<RouterVo> findUserMenuByUserId(Long userId) {
        List<SysMenu> sysMenusList = null;
        if (userId.longValue() == 1) {
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus, 1);
            queryWrapper.orderByAsc(SysMenu::getSortValue);
            sysMenusList = baseMapper.selectList(queryWrapper);
        } else {
            // 1.2、如果不是管理员，根据 userId 查询可以操作菜单列表
            // 多表关联查询:sys_role、sys_role_menu、sys_menu
            sysMenusList = baseMapper.findMenuListByUserId(userId);
        }
        // 2、把查询出来的数据列表， 构建成框架要求的路由结构
        // 先构建树形结构
        List<SysMenu> sysMenuTreeList = SysMenuUtil.buildTree(sysMenusList);
        // 构建框架要求的路由结构
        List<RouterVo> routerList = buildRouter(sysMenuTreeList);

        return routerList;
    }

    private List<RouterVo> buildRouter(List<SysMenu> menus) {
        // 创建 list 集合，存值最终数据
        List<RouterVo> routers = new ArrayList<>();
        // menus 遍历
        for (SysMenu sysMenu : menus) {
            RouterVo routerVo = new RouterVo();
            routerVo.setHidden(false);
            routerVo.setPath(getRouterPath(sysMenu));
            routerVo.setAlwaysShow(false);
            routerVo.setComponent(sysMenu.getComponent());
            routerVo.setMeta(new MetaVo(sysMenu.getName(), sysMenu.getIcon()));
            List<SysMenu> children = sysMenu.getChildren();
            if (sysMenu.getType() == 1) {
                List<SysMenu> hiddenMenuList = children.stream().filter(item ->
                                !StringUtils.isEmpty(item.getComponent()))
                        .collect(Collectors.toList());
                for (SysMenu menu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setPath(getRouterPath(menu));
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setComponent(menu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(menu.getName(), menu.getIcon()));
                    hiddenRouter.setComponent(menu.getComponent());
                    routers.add(hiddenRouter);
                }
            } else {
                if (!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 0) {
                        routerVo.setAlwaysShow(true);
                    }
                    routerVo.setChildren(buildRouter(children));
                }
            }
            routers.add(routerVo);
        }
        return routers;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if (menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }

    @Override
    public List<String> findUserPermissionsByUserId(Long userId) {

        List<SysMenu> sysMenusList = null;
        if (userId.longValue() == 1) {
            // 查询所有菜单列表
            LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysMenu::getStatus, 1);
            sysMenusList = baseMapper.selectList(queryWrapper);
        } else {
            // 2、如果不是管理员，根据userId查询可以操作按钮列表
            // 多表关联查询:sys_role、sys_role_menu、sys_menu
            sysMenusList = baseMapper.findMenuListByUserId(userId);
        }

        // 3、从查询出来的数据里面，获取可以操作按钮值的List集合，返回
        List<String> permsList = sysMenusList.stream()
                .filter(item -> item.getType() == 2)
                .map(item -> item.getPerms())
                .collect(Collectors.toList());
        return permsList;
    }
}