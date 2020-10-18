package com.tactbug.ddd.stock.outbound.repository.stock.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockEntityDao extends JpaRepository<StockEntity, StockId> {
    List<StockEntity> findAllByGoodsId(Long goodsId);
    List<StockEntity> findAllByWarehouseId(Long warehouseId);
}
