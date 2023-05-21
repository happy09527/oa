package com.example.common.config.exception;

import com.example.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author: ZhangX
 * @createDate: 2023/5/21
 * @description: 自定义异常类
 */
@Data
public class MyException extends RuntimeException{

    private Integer code;

    private String msg;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param code
     * @param msg
     */
    public MyException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public MyException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "MyException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
