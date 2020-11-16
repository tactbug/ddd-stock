package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.message.command.CallBackMessage;
import com.tactbug.ddd.stock.assist.message.command.CommandMessage;
import com.tactbug.ddd.stock.assist.message.command.order.OrderCommandTypeEnum;
import com.tactbug.ddd.stock.assist.message.command.order.ourSellingGoods.OurSellingCallBack;
import com.tactbug.ddd.stock.assist.message.command.order.ourSellingGoods.OurSellingCommand;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import com.tactbug.ddd.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCommandAdapter {

    @Autowired
    private StockService stockService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void onOurSelling(String data) {
        CommandMessage<OrderCommandTypeEnum, OurSellingCommand> commandMessage = JacksonUtil.stringToObject(data, new TypeReference<CommandMessage<OrderCommandTypeEnum, OurSellingCommand>>() {
        });
        OurSellingCommand ourSellingCommand = commandMessage.getCommandMessage();
        CallBackMessage<OurSellingCallBack> callBackMessage = stockService.getStockOutByOurSelling(ourSellingCommand.getGoodsId(), ourSellingCommand.getQuantity());
        String callBack = JacksonUtil.objectToString(callBackMessage);
        kafkaTemplate.send(
                commandMessage.getCallBackTopics().getName(),
                commandMessage.getCallBackTopics().getKey(),
                callBack
        );
    }
}
