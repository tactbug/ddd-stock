package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.goods.*;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import com.tactbug.ddd.stock.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoodsEventAdapter {

    @Autowired
    private StockService stockService;

    public void onGoodsCreated(String data) {
        EventMessage<GoodsEventTypeEnum, GoodsCreated> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, GoodsCreated>>() {
        });
        GoodsCreated goodsCreated = eventMessage.getEvent();
        stockService.createStockBySeller(goodsCreated.getStoreId(), goodsCreated.getGoodsId(), goodsCreated.getQuantity());
    }

    public void onGoodsBaned(String data) {
        EventMessage<GoodsEventTypeEnum, GoodsBaned> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, GoodsBaned>>() {
        });
        GoodsBaned goodsBaned = eventMessage.getEvent();
        stockService.banStockBySeller(goodsBaned.getGoodsId());
    }

    public void onQuantityUpdated(String data) {
        EventMessage<GoodsEventTypeEnum, QuantityUpdated> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<GoodsEventTypeEnum, QuantityUpdated>>() {
        });
        QuantityUpdated quantityUpdated = eventMessage.getEvent();
        stockService.updateStockQuantityBySeller(quantityUpdated.getSellerId(), quantityUpdated.getGoodsId(), quantityUpdated.getQuantity());
    }

}
