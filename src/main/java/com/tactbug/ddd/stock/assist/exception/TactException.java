package com.tactbug.ddd.stock.assist.exception;

import com.tactbug.ddd.stock.assist.vo.BaseVo;

public class TactException extends RuntimeException implements BaseVo {

    private final String code;

    public TactException(String code, String message){
        super(message);
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return this.getMessage();
    }
}
