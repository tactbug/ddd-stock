package com.tactbug.ddd.stock.outbound.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.stock.*;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WarehouseEventPublisher {

    @Value("${topic.warehouse.event}")
    private String warehouseEventTopic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void warehouseCreatedEvent(EventMessage<WarehouseEventTypeEnum, WarehouseCreated> eventMessage) throws IOException {
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(warehouseEventTopic, WarehouseEventTypeEnum.WAREHOUSE_CREATED.toString(), message);
    }

    public void childAddEvent(EventMessage<WarehouseEventTypeEnum, ChildAdded> eventMessage) throws JsonProcessingException {
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(warehouseEventTopic, WarehouseEventTypeEnum.CHILD_ADDED.toString(), message);
    }

    public void warehouseNameUpdatedEvent(EventMessage<WarehouseEventTypeEnum, WarehouseNameUpdated> eventMessage) throws JsonProcessingException {
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(warehouseEventTopic, WarehouseEventTypeEnum.WAREHOUSE_NAME_UPDATED.toString(), message);
    }

    public void warehouseMovedEvent(EventMessage<WarehouseEventTypeEnum, WarehouseMoved> eventMessage) throws JsonProcessingException {
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(warehouseEventTopic, WarehouseEventTypeEnum.WAREHOUSE_MOVED.toString(), message);
    }

    public void warehouseFullEvent(EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> eventMessage) throws JsonProcessingException {
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(warehouseEventTopic, WarehouseEventTypeEnum.WAREHOUSE_FULL.toString(), message);
    }

    public void warehouseOffEvent(EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> eventMessage) throws JsonProcessingException {
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(warehouseEventTopic, WarehouseEventTypeEnum.WAREHOUSE_OFF.toString(), message);
    }

    public void warehouseActiveEvent(EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> eventMessage) throws JsonProcessingException {
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(warehouseEventTopic, WarehouseEventTypeEnum.WAREHOUSE_ACTIVE.toString(), message);
    }

    public void warehouseDeleteEvent(EventMessage<WarehouseEventTypeEnum, WarehouseDeleted> eventMessage) throws JsonProcessingException {
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(warehouseEventTopic, WarehouseEventTypeEnum.WAREHOUSE_DELETED.toString(), message);
    }
}
