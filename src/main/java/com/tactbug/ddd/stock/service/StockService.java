package com.tactbug.ddd.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tactbug.ddd.stock.assist.message.command.CallBackMessage;
import com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods.OurSellingCallBack;

import java.io.IOException;

public interface StockService {

    void createWarehouse(String name, Integer type) throws IOException;
    void addChild(Long parentId, Integer warehouseType) throws JsonProcessingException;
    void updateWarehouseName(Long warehouseId, String newName) throws JsonProcessingException;
    void moveWarehouse(Long sourceId, Long targetId) throws JsonProcessingException;
    void makeWarehouseFull(Long warehouseId) throws JsonProcessingException;
    void deleteWarehouse(Long warehouseId) throws JsonProcessingException;
    void makeWarehouseOff(Long warehouseId) throws JsonProcessingException;
    void makeWarehouseOn(Long warehouseId) throws JsonProcessingException;
    void putStockInByManager(Long goodsId, Long warehouseId, Integer batch, Integer quantity);
    void createStockBySeller(Long sellerId, Long goodsId, Integer quantity);
    void addAreaBySellerOpeningAShop(Long storeId);
    void getStockOutBySellerSelling(Long goodsId, Integer quantity);
    void banStockBySeller(Long goodsId);
    void getStockOutByManager(Long goodsId, Long warehouseId, Integer batch, Integer quantity);
    CallBackMessage<OurSellingCallBack> getStockOutByOurSelling(Long goodsId, Integer quantity);
    void updateStockQuantityBySeller(Long goodsId, Integer quantity);
    void sellerCloseStore(Long sellerId);

}
