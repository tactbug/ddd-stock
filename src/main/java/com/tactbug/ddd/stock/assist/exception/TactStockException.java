package com.tactbug.ddd.stock.assist.exception;

public class TactStockException extends RuntimeException{

    private final String CODE = "S0101";

    public TactStockException(String message){
        super("库存服务异常: " + message);
    }

    @Override
    public String toString(){
        return "TactStockException: {" +
                "code: " + CODE +
                ", message: " + this.getMessage() + "}";
    }

    public String code() {
        return CODE;
    }

    public String message() {
        return this.getMessage();
    }
}
