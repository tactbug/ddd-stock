package com.tactbug.ddd.stock.assist.message.command;

import lombok.Data;

@Data
public class CallBackTopics {
    private String name;
    private String key;

    public static CallBackTopics getInstance(String name, String key){
        CallBackTopics callBackTopics = new CallBackTopics();
        callBackTopics.setName(name);
        callBackTopics.setKey(key);
        return callBackTopics;
    }
}
