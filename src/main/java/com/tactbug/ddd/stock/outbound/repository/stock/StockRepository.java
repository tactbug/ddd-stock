package com.tactbug.ddd.stock.outbound.repository.stock;

import com.tactbug.ddd.stock.aggregate.root.StockRoot;

import java.util.List;

public interface StockRepository {
    StockRoot getById(Long goodsId, Long warehouseId, Integer batch);
    void putStockIn(StockRoot stock);
    void putStockIn(List<StockRoot> stockList);
    void delete(StockRoot stock);
    void delete(List<StockRoot> stockList);
    List<StockRoot> getByGoods(Long goodsId);
    List<StockRoot> getByWarehouse(Long warehouse);
}
