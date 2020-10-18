package com.tactbug.ddd.stock.assist.message.command;

import lombok.Data;

@Data
public class CommandMessage<T, M> {

    private T commandType;
    private M commandMessage;
    private CallBackTopics callBackTopics;

}
