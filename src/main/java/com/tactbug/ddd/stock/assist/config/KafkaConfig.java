package com.tactbug.ddd.stock.assist.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;


@Configuration
public class KafkaConfig {

    @Value("${topic.warehouse.event}")
    private String warehouseEvent;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic warehouseEvent(){
        return TopicBuilder.name(warehouseEvent)
                .build();
    }

    //演示用主题
    @Bean
    public NewTopic orderCommand(){
        return TopicBuilder.name("order_command")
                .build();
    }

    //演示用主题
    @Bean
    public NewTopic orderCommandCallBack(){
        return TopicBuilder.name("order_command_callback")
                .build();
    }

    //演示用主题
    @Bean
    public NewTopic goodsEvent(){
        return TopicBuilder.name("goods_event")
                .build();
    }

    //演示用主题
    @Bean
    public NewTopic sellerEvent(){
        return TopicBuilder.name("seller_event")
                .build();
    }

    //演示用主题
    @Bean
    public NewTopic orderEvent(){
        return TopicBuilder.name("order_event")
                .build();
    }
}
