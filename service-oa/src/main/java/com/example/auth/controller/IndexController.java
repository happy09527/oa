package com.example.auth.controller;

import com.example.auth.service.SysUserService;
import com.example.common.result.Result;
import com.example.vo.system.LoginVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: ZhangX
 * @createDate: 2023/5/28
 * @description:
 */

@Api(tags = "首页")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {
    @Autowired
    SysUserService sysUserService;
    @ApiOperation("登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        String token = sysUserService.login(loginVo);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return Result.ok(map);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        String token = request.getHeader("token");
        Map<String,Object> map = sysUserService.getInfo(token);
        return Result.ok(map);
    }

    @ApiOperation("退出")
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
