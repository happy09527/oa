package com.example.process.service;

import com.example.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-15
 */
public interface ProcessRecordService extends IService<ProcessRecord> {
    void record(Long processId, Integer status, String description);
}
