package com.example.process.service.impl;

import com.example.auth.service.SysUserService;
import com.example.model.process.ProcessRecord;
import com.example.model.system.SysUser;
import com.example.process.mapper.ProcessRecordMapper;
import com.example.process.service.ProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-15
 */
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void record(Long processId, Integer status, String description) {
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUserId(sysUser.getId());
        processRecord.setOperateUser(sysUser.getName());
        baseMapper.insert(processRecord);
    }
}