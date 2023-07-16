package com.example.process.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.result.Result;
import com.example.model.process.ProcessTemplate;
import com.example.model.process.ProcessType;
import com.example.model.process.Process;
import com.example.process.service.ProcessService;
import com.example.process.service.ProcessTemplateService;
import com.example.process.service.ProcessTypeService;
import com.example.vo.process.ApprovalVo;
import com.example.vo.process.ProcessFormVo;
import com.example.vo.process.ProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: ZhangX
 * @createDate: 2023/7/13
 * @description:
 */
@RestController
@Api(tags = "审批流管理")
@RequestMapping(value = "/admin/process")
@CrossOrigin // 跨域
public class ProcessApiController {
    @Autowired
    private ProcessTypeService processTypeService;
    @Autowired
    private ProcessTemplateService processTemplateService;
    @Autowired
    private ProcessService processService;

    @ApiOperation(value = "待处理")
    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        return Result.ok(processService.findPending(pageParam));
    }

    @ApiOperation(value = "启动流程")
    @PostMapping("/startUp")
    public Result start(@RequestBody ProcessFormVo processFormVo) {
        processService.startUp(processFormVo);
        return Result.ok();
    }

    @GetMapping("findProcessType")
    @ApiOperation("获取全部审批分类及模板")
    public Result findProcessType() {
        List<ProcessType> processTypeList = processTypeService.findProcessType();
        return Result.ok(processTypeList);
    }

    @ApiOperation(value = "获取审批详情")
    @GetMapping("show/{id}")
    public Result show(@PathVariable Long id) {
        return Result.ok(processService.show(id));
    }

    @ApiOperation("获取审批模板")
    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable Long processTemplateId) {
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        return Result.ok(processTemplate);
    }

    @ApiOperation(value = "审批")
    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.ok();
    }

    @ApiOperation(value = "已处理")
    @GetMapping("/findProcessed/{page}/{limit}")
    public Result findProcessed(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        return Result.ok(processService.findProcessed(pageParam));
    }

    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        return Result.ok(processService.findStarted(pageParam));
    }
}
