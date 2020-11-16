package com.tactbug.ddd.stock.inbound.web;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.assist.vo.ResultResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(TactStockException.class)
    public ResultResponse tactExceptionHandler(TactStockException tactStockException){
        return ResultResponse.error(tactStockException);
    }

    @ExceptionHandler(Exception.class)
    public ResultResponse tactExceptionHandler(){
        return ResultResponse.error();
    }
}
