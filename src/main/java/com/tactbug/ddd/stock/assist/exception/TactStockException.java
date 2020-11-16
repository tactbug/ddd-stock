package com.tactbug.ddd.stock.assist.exception;


public class TactStockException extends TactException{

    private static final String CODE = "S0101";

    public TactStockException(String message){
        super(CODE, "库存服务异常: " + message);
    }

    @Override
    public String toString(){
        return "TactStockException: {" +
                "code: " + CODE +
                ", message: " + this.getMessage() + "}";
    }

}
