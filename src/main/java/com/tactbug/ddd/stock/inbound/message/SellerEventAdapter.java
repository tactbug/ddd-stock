package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.seller.SellerEventTypeEnum;
import com.tactbug.ddd.stock.assist.message.event.seller.ShopOpened;
import com.tactbug.ddd.stock.assist.message.event.seller.StoreClosed;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import com.tactbug.ddd.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class SellerEventAdapter {

    @Autowired
    private StockService stockService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "${topic.seller.event}")
    public void goodsCommand(String data,
                             @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey) throws JsonProcessingException {
        SellerEventTypeEnum sellerEventTypeEnum = SellerEventTypeEnum.getType(messageKey);
        switch (sellerEventTypeEnum){
            case OPEN_SHOP:{
                onShopOpened(data);
                break;
            }
            case CLOSE_STORE:{
                onStoreClosed(data);
                break;
            }
            default:
                throw new TactStockException("命令类型错误");
        }
    }

    private void onShopOpened(String data) throws JsonProcessingException {
        EventMessage<SellerEventTypeEnum, ShopOpened> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<SellerEventTypeEnum, ShopOpened>>() {
        });
        stockService.addAreaBySellerOpeningAShop(eventMessage.getEvent().getSellerId());
    }

    private void onStoreClosed(String data) throws JsonProcessingException {
        EventMessage<SellerEventTypeEnum, StoreClosed> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<SellerEventTypeEnum, StoreClosed>>() {
        });
        stockService.sellerCloseStore(eventMessage.getEvent().getSellerId());
    }
}
