package com.tactbug.ddd.stock.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tactbug.ddd.stock.aggregate.Goods;
import com.tactbug.ddd.stock.aggregate.Seller;
import com.tactbug.ddd.stock.aggregate.root.StockRoot;
import com.tactbug.ddd.stock.aggregate.Warehouse;
import com.tactbug.ddd.stock.aggregate.factory.WarehouseFactory;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseStatusEnum;
import com.tactbug.ddd.stock.assist.message.command.CallBackMessage;
import com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods.OurSellingCallBack;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.stock.*;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.outbound.publisher.WarehouseEventPublisher;
import com.tactbug.ddd.stock.outbound.repository.goods.GoodsRepository;
import com.tactbug.ddd.stock.outbound.repository.seller.SellerRepository;
import com.tactbug.ddd.stock.outbound.repository.stock.StockRepository;
import com.tactbug.ddd.stock.outbound.repository.warehouse.WarehouseRepository;
import com.tactbug.ddd.stock.aggregate.specification.WarehouseSpecification;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseTypeEnum;
import com.tactbug.ddd.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class StockServiceImpl implements StockService {

    private static final Integer SELLER_BATCH = 1;

    @Value("${seller.warehouse}")
    private Long sellerWarehouse;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private WarehouseEventPublisher warehouseEventPublisher;

    @Override
    public void createWarehouse(String name, Integer type) throws IOException {
        Warehouse warehouse = WarehouseFactory.createTopWarehouse(name, type);
        EventMessage<WarehouseEventTypeEnum, WarehouseCreated> warehouseCreatedEventMessage = warehouse.warehouseCreated();
        warehouseRepository.putWarehouseIn(warehouse);
        warehouseEventPublisher.warehouseCreatedEvent(warehouseCreatedEventMessage);
    }

    @Override
    public void addChild(Long parentId, Integer warehouseType) throws JsonProcessingException {
        Warehouse parent = warehouseRepository.getSimple(parentId);
        warehouseRepository.assembleChildren(parent);
        Warehouse child = WarehouseFactory.createChild(parent, warehouseType);
        EventMessage<WarehouseEventTypeEnum, ChildAdded> childAddedEventMessage = parent.addChild(child);
        warehouseRepository.putWarehouseIn(child);
        warehouseEventPublisher.childAddEvent(childAddedEventMessage);
    }

    @Override
    public void updateWarehouseName(Long warehouseId, String newName) throws JsonProcessingException {
        Warehouse warehouse = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleChildren(warehouse);
        EventMessage<WarehouseEventTypeEnum, WarehouseNameUpdated> eventMessage = warehouse.updateName(newName);
        warehouseRepository.putWarehouseIn(warehouse);
        warehouseEventPublisher.warehouseNameUpdatedEvent(eventMessage);
    }

    @Override
    public void moveWarehouse(Long sourceId, Long targetId) throws JsonProcessingException {
        Warehouse child = warehouseRepository.getSimple(sourceId);
        warehouseRepository.assembleChildren(child);
        Warehouse parent = warehouseRepository.getSimple(targetId);
        warehouseRepository.assembleChildren(parent);
        EventMessage<WarehouseEventTypeEnum, WarehouseMoved> eventMessage = parent.acceptChild(child);
        warehouseRepository.putWarehouseIn(parent);
        warehouseEventPublisher.warehouseMovedEvent(eventMessage);
    }

    @Override
    public void makeWarehouseFull(Long warehouseId) throws JsonProcessingException {
        Warehouse warehouse = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleStockList(warehouse);
        EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> eventMessage = warehouse.makeFull();
        warehouseRepository.putWarehouseIn(warehouse);
        warehouseEventPublisher.warehouseFullEvent(eventMessage);
    }

    @Override
    public void deleteWarehouse(Long warehouseId) throws JsonProcessingException {
        Warehouse warehouse = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleChildrenAndStockList(warehouse);

        WarehouseDeleted warehouseDeleted = new WarehouseDeleted();
        warehouseDeleted.setName(warehouse.getName());
        warehouseDeleted.setStatus(warehouse.getWarehouseStatus().getStatus());
        warehouseDeleted.setStatusInfo(warehouse.getWarehouseStatus().getMessage());
        warehouseDeleted.setType(warehouse.getWarehouseType().getType());
        warehouseDeleted.setTypeInfo(warehouse.getWarehouseType().getMessage());
        warehouseDeleted.setDeleteTime(System.currentTimeMillis());

        EventMessage<WarehouseEventTypeEnum, WarehouseDeleted> eventMessage =
                new EventMessage<>();
        eventMessage.setAggregateId(warehouseId);
        eventMessage.setEvent(warehouseDeleted);
        eventMessage.setEventType(WarehouseEventTypeEnum.WAREHOUSE_DELETED);

        warehouseRepository.delete(warehouse);
        warehouseEventPublisher.warehouseDeleteEvent(eventMessage);
    }

    @Override
    public void makeWarehouseOff(Long warehouseId) throws JsonProcessingException {
        Warehouse warehouse = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleChildrenAndStockList(warehouse);
        EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> eventMessage = warehouse.makeOff();
        warehouseRepository.putWarehouseIn(warehouse);
        warehouseEventPublisher.warehouseOffEvent(eventMessage);
    }

    @Override
    public void makeWarehouseOn(Long warehouseId) throws JsonProcessingException {
        Warehouse target = warehouseRepository.getSimple(warehouseId);
        warehouseRepository.assembleChildren(target);
        List<Warehouse> list = warehouseRepository.parentList(target);
        for (Warehouse p :
                list) {
            if (p.getWarehouseStatus().equals(WarehouseStatusEnum.OFF)){
                throw new TactStockException("上级仓库还处于禁用状态");
            }
        }
        EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> eventMessage = target.makeActive();
        warehouseRepository.putWarehouseIn(target);
        warehouseEventPublisher.warehouseActiveEvent(eventMessage);
    }

    @Override
    public void putStockInByManager(Long goodsId, Long warehouseId, Integer batchNumber, Integer quantity) {
        StockRoot stock = new StockRoot(goodsId, warehouseId, batchNumber, quantity);
        Goods goods;
        if (goodsRepository.exists(goodsId)){
            goods = goodsRepository.getById(goodsId);
        }else {
            goods = Goods.create(goodsId);
        }
        goods.addStock(stock);
        goodsRepository.putGoodsIn(goods);
    }

    @Override
    public void createStockBySeller(Long sellerId, Long goodsId, Integer quantity) {
        if (!sellerRepository.exists(sellerId)){
            throw new TactStockException("当前店铺不存在");
        }
        Seller seller = sellerRepository.getById(sellerId);
        Warehouse store = warehouseRepository.getSimple(seller.getAreaId());
        Goods goods = Goods.create(goodsId);
        StockRoot stock = new StockRoot(goodsId, store.getId(), SELLER_BATCH, quantity);
        goods.getStocks().add(stock);
        goodsRepository.putGoodsIn(goods);
    }

    @Override
    public void addAreaBySellerOpeningAShop(Long storeId) {
        Warehouse warehouse = warehouseRepository.getSimple(sellerWarehouse);
        warehouseRepository.assembleChildren(warehouse);
        Warehouse area = WarehouseFactory.createChild(warehouse, WarehouseTypeEnum.AREA.getType());
        warehouse.addChild(area);
        warehouseRepository.putWarehouseIn(area);
        Seller seller = new Seller(storeId, area.getId(), new Date(), new Date());
        sellerRepository.putStoreIn(seller);
    }

    @Override
    public void getStockOutBySellerSelling(Long goodsId, Integer quantity) {
        Goods goods = goodsRepository.getById(goodsId);
        goods.sellerSelling(quantity);
        goods.checkGoodsStock();
        goodsRepository.putGoodsIn(goods);
    }

    @Override
    public void banStockBySeller(Long goodsId) {
        Goods goods = goodsRepository.getById(goodsId);
        goods.checkGoodsStock();
        goodsRepository.delete(goods);
    }

    @Override
    public void getStockOutByManager(Long goodsId, Long warehouseId, Integer batch, Integer quantity) {
        Goods goods = goodsRepository.getById(goodsId);
        goods.reduceStockQuantity(warehouseId, batch, quantity);
        goodsRepository.putGoodsIn(goods);
    }

    @Override
    public CallBackMessage<OurSellingCallBack> getStockOutByOurSelling(Long goodsId, Integer quantity) {
        Goods goods = goodsRepository.getById(goodsId);
        CallBackMessage<OurSellingCallBack> callBackMessage = goods.selfSelling(quantity);
        goods.checkGoodsStock();
        goodsRepository.putGoodsIn(goods);
        return callBackMessage;
    }

    @Override
    public void updateStockQuantityBySeller(Long goodsId, Integer quantity) {
        Goods goods = goodsRepository.getById(goodsId);
        goods.sellerSetQuantity(quantity);
        goods.checkGoodsStock();
        goodsRepository.putGoodsIn(goods);
    }

    @Override
    public void sellerCloseStore(Long sellerId) {
        Seller seller = sellerRepository.getById(sellerId);
        sellerRepository.delete(seller);
    }
}
