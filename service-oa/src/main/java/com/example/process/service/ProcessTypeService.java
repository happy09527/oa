package com.example.process.service;

import com.example.model.process.ProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-09
 */
public interface ProcessTypeService extends IService<ProcessType> {

    List<ProcessType> findProcessType();
}
