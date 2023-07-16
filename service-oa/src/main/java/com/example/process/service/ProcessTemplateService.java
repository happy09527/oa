package com.example.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.model.process.ProcessTemplate;

import java.util.List;

/**
 * <p>
 * 审批模板 服务类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-09
 */
public interface ProcessTemplateService extends IService<ProcessTemplate> {
    IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> pageParam);

    void publish(Long id);

    List<ProcessTemplate> getProcessTemplateByTypeId(Long typeId);
}
