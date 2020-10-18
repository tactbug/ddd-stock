package com.tactbug.ddd.stock.assist.config;

import com.tactbug.ddd.stock.assist.utils.SnowFlakeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBeanConfig {

    @Value("${center}")
    private Long center;

    @Value("${machine}")
    private Long machine;


    @Bean
    public SnowFlakeFactory getSnowFlake(){
        return new SnowFlakeFactory(center, machine);
    }
}
