package com.tactbug.ddd.stock.assist.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeUtil {

    private static SnowFlakeFactory snowFlakeFactory;

    @Autowired
    public void setSnowFlakeFactory(SnowFlakeFactory snowFlakeFactory){
        CodeUtil.snowFlakeFactory = snowFlakeFactory;
    }

    public static Long nextId(){
        return snowFlakeFactory.nextId();
    }
}
