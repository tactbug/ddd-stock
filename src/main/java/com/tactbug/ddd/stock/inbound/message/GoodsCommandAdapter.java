package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.assist.message.command.CallBackMessage;
import com.tactbug.ddd.stock.assist.message.command.CommandMessage;
import com.tactbug.ddd.stock.assist.message.command.goods.GoodsCommandTypeEnum;
import com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods.OurSellingCallBack;
import com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods.OurSellingCommand;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import com.tactbug.ddd.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class GoodsCommandAdapter {

    @Autowired
    private StockService stockService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.goods.command.callBack}")
    private String callBackTopic;

    @KafkaListener(topics = "${topic.goods.command.command}")
    public void goodsCommand(
            String data,
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String messageKey
    ) throws JsonProcessingException {
        GoodsCommandTypeEnum goodsCommandTypeEnum = GoodsCommandTypeEnum.getInstance(messageKey);
        switch (goodsCommandTypeEnum){
            case OUR_SELLING:{
                onOurSelling(data);
                break;
            }
            default:
                throw new TactStockException("没有相应的商品服务命令");
        }
    }

    private void onOurSelling(String data) throws JsonProcessingException {
        CommandMessage<GoodsCommandTypeEnum, OurSellingCommand> commandMessage = JacksonUtil.stringToObject(data, new TypeReference<CommandMessage<GoodsCommandTypeEnum, OurSellingCommand>>() {
        });
        OurSellingCommand ourSellingCommand = commandMessage.getCommandMessage();
        CallBackMessage<OurSellingCallBack> callBackMessage = stockService.getStockOutByOurSelling(ourSellingCommand.getGoodsId(), ourSellingCommand.getQuantity());
        String callBack = JacksonUtil.objectToString(callBackMessage);
        kafkaTemplate.send(
                callBackTopic,
                commandMessage.getCallBackTopics().getKey(),
                callBack
        );
    }
}
