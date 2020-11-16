package com.tactbug.ddd.stock.assist.message.command;

import lombok.Data;

@Data
public class CallBackMessage<T> {

    private Long messageId;
    private Boolean success;
    private String info;
    private T message;

    public static<T> CallBackMessage<T> success(T t){
        CallBackMessage<T> callBackMessage = new CallBackMessage<>();
        callBackMessage.setSuccess(true);
        callBackMessage.setMessage(t);
        callBackMessage.setInfo("success");
        return callBackMessage;
    }

    public static<T> CallBackMessage<T> error(String info){
        CallBackMessage<T> callBackMessage = new CallBackMessage<>();
        callBackMessage.setSuccess(false);
        callBackMessage.setInfo(info);
        callBackMessage.setMessage(null);
        return callBackMessage;
    }
}
