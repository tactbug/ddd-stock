package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.order.GoodsSold;
import com.tactbug.ddd.stock.assist.message.event.order.OrderEventTypeEnum;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import com.tactbug.ddd.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventAdapter {

    @Autowired
    private StockService stockService;

    public void onSellerSelling(String data) {
        EventMessage<OrderEventTypeEnum, GoodsSold> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<OrderEventTypeEnum, GoodsSold>>() {});
        GoodsSold goodsSold = eventMessage.getEvent();
        stockService.getStockOutBySellerSelling(goodsSold.getGoodsId(), goodsSold.getQuantity());
    }
}
