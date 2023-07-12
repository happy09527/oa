package com.example.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.model.process.Process;
import com.example.vo.process.ProcessQueryVo;
import com.example.vo.process.ProcessVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author Zhangx
 * @since 2023-07-11
 */
public interface ProcessMapper extends BaseMapper<Process> {
    IPage<ProcessVo> selectPage(Page<ProcessVo> page, @Param("vo") ProcessQueryVo processQueryVo);
}
