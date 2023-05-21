package com.example.common.config.exception;

import com.example.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: ZhangX
 * @createDate: 2023/5/21
 * @description: 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 全局异常处理 执行的方法
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail().message("执行全局处理异常...");
    }

    /**
     * 自定义异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException e){
        e.printStackTrace();
        return Result.fail().code(e.getCode()).message(e.getMsg());
    }
}