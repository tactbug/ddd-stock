package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.assist.message.command.CallBackMessage;
import com.tactbug.ddd.stock.assist.message.command.order.OrderCommandTypeEnum;
import com.tactbug.ddd.stock.assist.message.command.order.ourSellingGoods.OurSellingCallBack;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class MockCommandCallBackAccept {

    @KafkaListener(topics = "order_command_callBack")
    public void goodsCommandCallBack(
            String data,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String key
    ) {
        OrderCommandTypeEnum orderCommandTypeEnum = OrderCommandTypeEnum.getInstance(key);
        switch (orderCommandTypeEnum){
            case OUR_SELLING:
                CallBackMessage<OurSellingCallBack> message = JacksonUtil.stringToObject(data, new TypeReference<CallBackMessage<OurSellingCallBack>>() {
                });
                System.out.println(message);
                break;
        }
    }
}
