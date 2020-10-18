package com.tactbug.ddd.stock.inbound.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tactbug.ddd.stock.assist.message.command.CallBackTopics;
import com.tactbug.ddd.stock.assist.message.command.CommandMessage;
import com.tactbug.ddd.stock.assist.message.command.goods.GoodsCommandTypeEnum;
import com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods.OurSellingCommand;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.goods.*;
import com.tactbug.ddd.stock.assist.message.event.seller.SellerEventTypeEnum;
import com.tactbug.ddd.stock.assist.message.event.seller.ShopOpened;
import com.tactbug.ddd.stock.assist.message.event.seller.StoreClosed;
import com.tactbug.ddd.stock.assist.utils.JacksonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "模拟商品服务、卖家服务的命令式和广播消息")
@RestController
public class MockRadioAndCommand {

    @Value("${topic.seller.event}")
    private String sellerEvent;

    @Value("${topic.goods.event}")
    private String goodsEvent;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @ApiOperation(value = "模拟商品服务我方出售货品命令")
    @PutMapping("/goods/self/selling")
    public void ourSelling(Long goodsId, Integer quantity) throws JsonProcessingException {

        CommandMessage<GoodsCommandTypeEnum, OurSellingCommand> goodsCommand =
                new CommandMessage<>();
        OurSellingCommand ourSellingCommand = OurSellingCommand.getInstance(goodsId, quantity);

        goodsCommand.setCommandType(GoodsCommandTypeEnum.OUR_SELLING);
        goodsCommand.setCommandMessage(ourSellingCommand);
        CallBackTopics callBackTopics =
                CallBackTopics.getInstance("goods_command_callBack", goodsCommand.getCommandType().toString());
        goodsCommand.setCallBackTopics(callBackTopics);

        String message = JacksonUtil.objectToString(goodsCommand);

        kafkaTemplate.send("goods_command", GoodsCommandTypeEnum.OUR_SELLING.toString(), message);
    }

    @ApiOperation("模拟卖家服务创建店铺事件")
    @PostMapping("/seller/store")
    public void sellerOpenShop(Long sellerId) throws JsonProcessingException {
        ShopOpened shopOpened = new ShopOpened();
        shopOpened.setSellerId(sellerId);
        EventMessage<SellerEventTypeEnum, ShopOpened> eventMessage = new EventMessage<>();
        eventMessage.setAggregateId(sellerId);
        eventMessage.setEventType(SellerEventTypeEnum.OPEN_SHOP);
        eventMessage.setEvent(shopOpened);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(sellerEvent, eventMessage.getEventType().toString(), message);
    }

    @ApiOperation("模拟卖家服务关闭店铺事件")
    @DeleteMapping("/seller/store")
    public void sellerDeleteShop(Long sellerId) throws JsonProcessingException {
        StoreClosed storeClosed = new StoreClosed();
        storeClosed.setSellerId(sellerId);
        EventMessage<SellerEventTypeEnum, StoreClosed> eventMessage = new EventMessage<>();
        eventMessage.setAggregateId(sellerId);
        eventMessage.setEventType(SellerEventTypeEnum.CLOSE_STORE);
        eventMessage.setEvent(storeClosed);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(sellerEvent, eventMessage.getEventType().toString(), message);
    }

    @ApiOperation("模拟商品服务创建商品事件")
    @PostMapping("/goods")
    public void sellerCreateGoods(Long sellerId, Long goodsId, Integer quantity) throws JsonProcessingException {
        GoodsCreated goodsCreated = GoodsCreated.getInstance(sellerId, goodsId, quantity);
        EventMessage<GoodsEventTypeEnum, GoodsCreated> eventMessage = new EventMessage<>();
        eventMessage.setAggregateId(goodsId);
        eventMessage.setEventType(GoodsEventTypeEnum.SELLER_CREATE_GOODS);
        eventMessage.setEvent(goodsCreated);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(goodsEvent, eventMessage.getEventType().toString(), message);
    }

    @ApiOperation("模拟商品服务删除商品事件")
    @DeleteMapping("/goods")
    public void sellerBanGoods(Long goodsId) throws JsonProcessingException {
        GoodsBaned goodsBaned = new GoodsBaned();
        goodsBaned.setGoodsId(goodsId);
        EventMessage<GoodsEventTypeEnum, GoodsBaned> eventMessage = new EventMessage<>();
        eventMessage.setAggregateId(goodsId);
        eventMessage.setEventType(GoodsEventTypeEnum.SELLER_BAN_GOODS);
        eventMessage.setEvent(goodsBaned);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(goodsEvent, eventMessage.getEventType().toString(), message);
    }

    @ApiOperation("模拟商品服务商品售卖事件")
    @PutMapping("/goods")
    public void sellerSellingGoods(Long goodsId, Integer quantity) throws JsonProcessingException {
        GoodsSold goodsSold = new GoodsSold();
        goodsSold.setGoodsId(goodsId);
        goodsSold.setQuantity(quantity);
        EventMessage<GoodsEventTypeEnum, GoodsSold> eventMessage = new EventMessage<>();
        eventMessage.setAggregateId(goodsId);
        eventMessage.setEventType(GoodsEventTypeEnum.SELLER_SELLING);
        eventMessage.setEvent(goodsSold);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(goodsEvent, eventMessage.getEventType().toString(), message);
    }

    @ApiOperation("模拟商品服务修改商品库存数量事件")
    @PutMapping("/goods/quantity")
    public void sellerUpdateGoodsQuantity(Long goodsId, Integer quantity) throws JsonProcessingException {
        QuantityUpdated quantityUpdated = new QuantityUpdated();
        quantityUpdated.setGoodsId(goodsId);
        quantityUpdated.setQuantity(quantity);
        EventMessage<GoodsEventTypeEnum, QuantityUpdated> eventMessage = new EventMessage<>();
        eventMessage.setAggregateId(goodsId);
        eventMessage.setEventType(GoodsEventTypeEnum.SELLER_UPDATE_QUANTITY);
        eventMessage.setEvent(quantityUpdated);
        String message = JacksonUtil.objectToString(eventMessage);
        kafkaTemplate.send(goodsEvent, eventMessage.getEventType().toString(), message);
    }
}
