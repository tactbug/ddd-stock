package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.message.command.order.OrderCommandTypeEnum;
import com.tactbug.ddd.stock.assist.message.event.goods.GoodsEventTypeEnum;
import com.tactbug.ddd.stock.assist.message.event.order.OrderEventTypeEnum;
import com.tactbug.ddd.stock.assist.message.event.seller.SellerEventTypeEnum;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import com.tactbug.ddd.stock.inbound.message.messageBox.Message;
import com.tactbug.ddd.stock.inbound.message.messageBox.MessageJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Component
@Transactional
public class MessageDispatch {

    private static final String MESSAGE_ID_KEY = "messageId";

    @Autowired
    private MessageJpa messageJpa;

    @Autowired
    private GoodsEventAdapter goodsEventAdapter;

    @Autowired
    private OrderEventAdapter orderEventAdapter;

    @Autowired
    private SellerEventAdapter sellerEventAdapter;

    @Autowired
    private OrderCommandAdapter orderCommandAdapter;

    @KafkaListener(topics = "${topic.goods.event}")
    public void goodsEvent(String data,
                             @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey) {
        if (checkDuplicate(data)){
            return;
        }
        GoodsEventTypeEnum goodsEventTypeEnum = GoodsEventTypeEnum.getType(messageKey);
        switch (goodsEventTypeEnum){
            case SELLER_CREATE_GOODS:{
                goodsEventAdapter.onGoodsCreated(data);
                break;
            }
            case SELLER_BAN_GOODS:{
                goodsEventAdapter.onGoodsBaned(data);
                break;
            }
            case SELLER_UPDATE_QUANTITY:{
                goodsEventAdapter.onQuantityUpdated(data);
                break;
            }
        }
    }

    @KafkaListener(topics = "${topic.order.command}")
    public void orderCommand(
            String data,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey
    ) {
        if (checkDuplicate(data)){
            return;
        }
        OrderCommandTypeEnum orderCommandTypeEnum = OrderCommandTypeEnum.getInstance(messageKey);
        switch (orderCommandTypeEnum){
            case OUR_SELLING:{
                orderCommandAdapter.onOurSelling(data);
                break;
            }
        }
    }

    @KafkaListener(topics = "${topic.order.event}")
    public void orderEvent(String data,
                           @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey) {
        if (checkDuplicate(data)){
            return;
        }
        OrderEventTypeEnum orderEventTypeEnum = OrderEventTypeEnum.getType(messageKey);
        switch (orderEventTypeEnum){
            case SELLER_SELLING:{
                orderEventAdapter.onSellerSelling(data);
                break;
            }
        }
    }

    @KafkaListener(topics = "${topic.seller.event}")
    public void sellerEvent(String data,
                             @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey) {
        if (checkDuplicate(data)){
            return;
        }
        SellerEventTypeEnum sellerEventTypeEnum = SellerEventTypeEnum.getType(messageKey);
        switch (sellerEventTypeEnum){
            case OPEN_SHOP:{
                sellerEventAdapter.onShopOpened(data);
                break;
            }
            case CLOSE_STORE:{
                sellerEventAdapter.onStoreClosed(data);
                break;
            }
        }
    }

    private boolean checkDuplicate(String data){
        Optional<Message> optional =
                messageJpa.findById(messageId(data));
        return optional.isPresent();
    }

    private Long messageId(String data){
        Map<String, String> map = JacksonUtil.stringToObject(data, new TypeReference<Map<String, String>>() {
        });
        String id = map.get(MESSAGE_ID_KEY);
        return Long.valueOf(id);
    }
}
