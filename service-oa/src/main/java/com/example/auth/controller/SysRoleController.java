package com.example.auth.controller;

import com.example.auth.service.SysRoleService;
import com.example.common.result.Result;
import com.example.model.system.SysRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result findAll(){
        return Result.ok(sysRoleService.list());
    }
}
