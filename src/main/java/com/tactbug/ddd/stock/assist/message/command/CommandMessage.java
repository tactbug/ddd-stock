package com.tactbug.ddd.stock.assist.message.command;

import com.tactbug.ddd.stock.assist.utils.CodeUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommandMessage<T, M> {

    private Long messageId;
    private T commandType;
    private M commandMessage;
    private CallBackTopics callBackTopics;

    public CommandMessage(T commandType, M commandMessage, CallBackTopics callBackTopics){
        messageId = CodeUtil.nextId();
        this.commandType = commandType;
        this.commandMessage = commandMessage;
        this.callBackTopics = callBackTopics;
    }

}
