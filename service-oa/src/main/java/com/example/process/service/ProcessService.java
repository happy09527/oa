package com.example.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.process.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.process.ProcessQueryVo;
import com.example.vo.process.ProcessVo;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-11
 */
public interface ProcessService extends IService<Process> {
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    void deployByZip(String deployPath);
}
