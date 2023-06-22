package com.example.auth.controller;

import com.example.auth.service.SysMenuService;
import com.example.common.result.Result;
import com.example.model.system.SysMenu;
import com.example.vo.system.AssignMenuVo;
import com.example.vo.system.AssignRoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author Zhangx
 * @since 2023-06-11
 */
@Api(tags = "菜单管理模块")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {
    @Autowired
    private SysMenuService sysMenuService;


    @ApiOperation(value = "菜单列表")
    @GetMapping("/findNodes")
    public Result findNodes() {
        List<SysMenu> list = sysMenuService.findNodes();
        return Result.ok(list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody SysMenu sysMenu) {
        sysMenuService.save(sysMenu);
        return Result.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PostMapping("update")
    public Result updateById(@RequestBody SysMenu sysMenu) {
        sysMenuService.updateById(sysMenu);
        return Result.ok();
    }

    @ApiOperation(value = "删除菜单")
    @PostMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysMenuService.removeMenuById(id);
        return Result.ok();
    }

    // 1、查询所有角色 和 当前用户所属角色
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/toAssign/{roleId}")
    public Result toAssign(@PathVariable Long roleId) {
        List<SysMenu> sysMenus = sysMenuService.findMenuByRoleId(roleId);
        return Result.ok(sysMenus);
    }

    // 2、为用户分配角色
    @ApiOperation("为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignMenuVo assignMenuVo) {
        sysMenuService.doAssign(assignMenuVo);
        return Result.ok();
    }
}