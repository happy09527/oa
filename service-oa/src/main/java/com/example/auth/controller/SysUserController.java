package com.example.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.auth.service.SysUserService;
import com.example.common.result.Result;
import com.example.model.system.SysUser;
import com.example.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.security.provider.MD5;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Zhangx
 * @since 2023-06-01
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation("分页查询用户")
    @GetMapping("{page}/{limit}")
    public Result pageQuery(@PathVariable("page") Long page,
                            @PathVariable("limit") Long limit,
                            SysUserQueryVo sysUserQueryVo) {
        IPage<SysUser> iPage = new Page<>(page, limit);
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(sysUserQueryVo.getKeyword()), SysUser::getName, sysUserQueryVo.getKeyword());
        queryWrapper.ge(!StringUtils.isEmpty(sysUserQueryVo.getCreateTimeBegin()), SysUser::getCreateTime, sysUserQueryVo.getCreateTimeBegin());
        queryWrapper.le(!StringUtils.isEmpty(sysUserQueryVo.getCreateTimeEnd()), SysUser::getCreateTime, sysUserQueryVo.getCreateTimeEnd());
        sysUserService.page(iPage, queryWrapper);
        return Result.ok(iPage);
    }

    @ApiOperation(value = "获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "保存用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser user) {
        boolean isSuccess = sysUserService.saveUser(user);
        if(isSuccess){
            return Result.ok();
        }
        return Result.fail();
    }

    @ApiOperation(value = "更新用户")
    @PostMapping("update")
    public Result updateById(@RequestBody SysUser user) {
        sysUserService.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @PostMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    @ApiOperation(value = "更改用户状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id,
                               @PathVariable Integer status){
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }
}