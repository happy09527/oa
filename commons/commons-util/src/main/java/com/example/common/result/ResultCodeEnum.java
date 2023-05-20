package com.example.common.result;

import lombok.Data;
import lombok.Getter;

/**
 * @author: ZhangX
 * @createDate: 2023/5/20
 * @description:
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(202, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限");

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
