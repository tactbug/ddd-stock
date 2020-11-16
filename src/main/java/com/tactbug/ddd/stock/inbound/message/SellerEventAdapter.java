package com.tactbug.ddd.stock.inbound.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.seller.SellerEventTypeEnum;
import com.tactbug.ddd.stock.assist.message.event.seller.ShopOpened;
import com.tactbug.ddd.stock.assist.message.event.seller.StoreClosed;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import com.tactbug.ddd.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SellerEventAdapter {

    @Autowired
    private StockService stockService;

    public void onShopOpened(String data) {
        EventMessage<SellerEventTypeEnum, ShopOpened> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<SellerEventTypeEnum, ShopOpened>>() {
        });
        stockService.addAreaBySellerOpeningAShop(eventMessage.getEvent().getSellerId());
    }

    public void onStoreClosed(String data) {
        EventMessage<SellerEventTypeEnum, StoreClosed> eventMessage = JacksonUtil.stringToObject(data, new TypeReference<EventMessage<SellerEventTypeEnum, StoreClosed>>() {
        });
        stockService.sellerCloseStore(eventMessage.getEvent().getSellerId());
    }
}
