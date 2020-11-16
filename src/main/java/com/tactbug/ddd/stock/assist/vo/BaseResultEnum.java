package com.tactbug.ddd.stock.assist.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BaseResultEnum implements BaseVo{
    OK("00000", "操作成功"),
    SYSTEM_ERROR("A0001", "系统异常"),
    JSON_ERROR("A0101", "json解析异常"),
    NETWORK_ERROR("A0102", "网络异常"),
    INTERFACE_ERROR("A0103", "接口访问异常"),
    ;

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    private final String code;
    private final String message;
}
