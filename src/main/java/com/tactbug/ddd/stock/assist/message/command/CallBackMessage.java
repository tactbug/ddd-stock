package com.tactbug.ddd.stock.assist.message.command;

import lombok.Data;

@Data
public class CallBackMessage<T> {
    private Boolean success;
    private T message;

    public static<T> CallBackMessage<T> success(T t){
        CallBackMessage<T> callBackMessage = new CallBackMessage<T>();
        callBackMessage.setSuccess(true);
        callBackMessage.setMessage(t);
        return callBackMessage;
    }
}
