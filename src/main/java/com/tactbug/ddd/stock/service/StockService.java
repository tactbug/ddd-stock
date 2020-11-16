package com.tactbug.ddd.stock.service;

import com.tactbug.ddd.stock.assist.message.command.CallBackMessage;
import com.tactbug.ddd.stock.assist.message.command.order.ourSellingGoods.OurSellingCallBack;

public interface StockService {

    void createWarehouse(String name, Integer type);
    void addChild(Long parentId, Integer warehouseType);
    void updateWarehouseName(Long warehouseId, String newName);
    void moveWarehouse(Long sourceId, Long targetId);
    void makeWarehouseFull(Long warehouseId);
    void deleteWarehouse(Long warehouseId);
    void makeWarehouseOff(Long warehouseId);
    void makeWarehouseOn(Long warehouseId);
    void putStockInByManager(Long goodsId, Long warehouseId, Integer batch, Integer quantity);
    void createStockBySeller(Long sellerId, Long goodsId, Integer quantity);
    void addAreaBySellerOpeningAShop(Long sellerId);
    void getStockOutBySellerSelling(Long goodsId, Integer quantity);
    void banStockBySeller(Long goodsId);
    void getStockOutByManager(Long goodsId, Long warehouseId, Integer batch, Integer quantity);
    CallBackMessage<OurSellingCallBack> getStockOutByOurSelling(Long goodsId, Integer quantity);
    void updateStockQuantityBySeller(Long sellerId, Long goodsId, Integer quantity);
    void sellerCloseStore(Long sellerId);

}
