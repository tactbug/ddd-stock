package com.tactbug.ddd.stock.assist.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${topic.warehouse.event}")
    private String warehouseEvent;

    @Value("${topic.goods.event}")
    private String goodsEvent;

    @Value("${topic.seller.event}")
    private String sellerEvent;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.137.101:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public NewTopic warehouseEvent(){
        return TopicBuilder.name(warehouseEvent)
                .build();
    }

    @Bean
    public NewTopic goodsCommand(){
        return TopicBuilder.name("goods_command")
                .build();
    }

    @Bean
    public NewTopic goodsCommandCallBack(){
        return TopicBuilder.name("goods_command_callBack")
                .build();
    }

    @Bean
    public NewTopic goodsEvent(){
        return TopicBuilder.name(goodsEvent)
                .build();
    }

    @Bean
    public NewTopic sellerEvent(){
        return TopicBuilder.name(sellerEvent)
                .build();
    }

}
