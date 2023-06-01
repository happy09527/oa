package com.example.auth.controller;

import com.example.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation("登录")
    @PostMapping("login")
    public Result login() {
        Map<String, Object> map = new HashMap<>();
        map.put("token", "admin");
        return Result.ok(map);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("info")
    public Result info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", "admin");
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.ok(map);
    }

    @ApiOperation("退出")
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }
}
