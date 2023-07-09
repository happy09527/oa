package com.example.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auth.service.SysRoleService;
import com.example.common.result.Result;
import com.example.model.system.SysRole;
import com.example.vo.system.AssignRoleVo;
import com.example.vo.system.SysRoleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: ZhangX
 * @createDate: 2023/5/20
 * @description:
 */

@Api(tags = "角色管理")
@RestController
@RequestMapping("admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("查询所有角色")
    @GetMapping("/getAll")
    public Result getAll() {
        return Result.ok(sysRoleService.list());
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("分页查询角色")
    @GetMapping("/{page}/{limit}")
    public Result pageQuery(@PathVariable("page") Long page,
                            @PathVariable("limit") Long limit,
                            SysRoleQueryVo sysRoleQueryVo) {
        Page<SysRole> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(sysRoleQueryVo.getRoleName()), SysRole::getRoleName, sysRoleQueryVo.getRoleName());
        sysRoleService.page(pageParam, wrapper);
        return Result.ok(pageParam);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation(value = "根据id获取角色")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        return Result.ok(role);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation(value = "新增角色")
    @PostMapping("save")
    public Result save(@RequestBody @Validated SysRole role) {
        boolean result = sysRoleService.save(role);
        if (result) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation(value = "修改角色")
    @PostMapping("update")
    public Result updateById(@RequestBody SysRole role) {
        boolean result = sysRoleService.updateById(role);
        if (result) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "删除角色")
    @PostMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean result = sysRoleService.removeById(id);
        if (result) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation(value = "根据id列表删除角色")
    @PostMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean result = sysRoleService.removeByIds(idList);
        if (result) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 1、查询所有角色 和 当前用户所属角色
//    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String, Object> map = sysRoleService.findRoleDataByUserId(userId);
        return Result.ok(map);
    }

    // 2、为用户分配角色
    @PreAuthorize("hasAuthority('bnt.sysUser.assignRole')")
    @ApiOperation("为用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssignRoleVo assignRoleVo) {
        sysRoleService.doAssign(assignRoleVo);
        return Result.ok();
    }

}