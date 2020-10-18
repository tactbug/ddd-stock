package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.assist.message.command.CallBackMessage;
import com.tactbug.ddd.stock.assist.message.command.goods.GoodsCommandTypeEnum;
import com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods.OurSellingCallBack;
import com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods.OurSellingCommand;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.goods.*;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import com.tactbug.ddd.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class GoodsEventAdapter {

    @Autowired
    private StockService stockService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "${topic.goods.event}")
    public void goodsCommand(String data,
                             @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey) throws JsonProcessingException {
        GoodsEventTypeEnum goodsEventTypeEnum = GoodsEventTypeEnum.getType(messageKey);
        switch (goodsEventTypeEnum){
            case SELLER_CREATE_GOODS:{
                onGoodsCreated(data);
                break;
            }
            case SELLER_SELLING:{
                onGoodsSold(data);
                break;
            }
            case SELLER_BAN_GOODS:{
                onGoodsBaned(data);
                break;
            }
            case SELLER_UPDATE_QUANTITY:{
                onQuantityUpdated(data);
                break;
            }
            default:
                throw new TactStockException("命令类型错误");
        }
    }

    private void onGoodsCreated(String data) throws JsonProcessingException {
        EventMessage<GoodsEventTypeEnum, GoodsCreated> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, GoodsCreated>>() {
        });
        GoodsCreated goodsCreated = eventMessage.getEvent();
        stockService.createStockBySeller(goodsCreated.getStoreId(), goodsCreated.getGoodsId(), goodsCreated.getQuantity());
    }

    private void onGoodsBaned(String data) throws JsonProcessingException {
        EventMessage<GoodsEventTypeEnum, GoodsBaned> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, GoodsBaned>>() {
        });
        GoodsBaned goodsBaned = eventMessage.getEvent();
        stockService.banStockBySeller(goodsBaned.getGoodsId());
    }

    private void onGoodsSold(String data) throws JsonProcessingException {
        EventMessage<GoodsEventTypeEnum, GoodsSold> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, GoodsSold>>() {
        });
        GoodsSold goodsSold = eventMessage.getEvent();
        stockService.getStockOutBySellerSelling(goodsSold.getGoodsId(), goodsSold.getQuantity());
    }

    private void onQuantityUpdated(String data) throws JsonProcessingException {
        EventMessage<GoodsEventTypeEnum, QuantityUpdated> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, QuantityUpdated>>() {
        });
        QuantityUpdated quantityUpdated = eventMessage.getEvent();
        stockService.updateStockQuantityBySeller(quantityUpdated.getGoodsId(), quantityUpdated.getQuantity());
    }
}
