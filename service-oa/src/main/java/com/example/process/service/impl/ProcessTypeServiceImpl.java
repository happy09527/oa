package com.example.process.service.impl;

import com.example.model.process.ProcessType;
import com.example.process.mapper.ProcessTypeMapper;
import com.example.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-09
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {

}
