package com.tactbug.ddd.stock.outbound.repository.warehouse;

import com.tactbug.ddd.stock.aggregate.Warehouse;

import java.util.List;

public interface WarehouseRepository {
    boolean exists(Long id);
    Warehouse getSimple(Long id);
    List<Warehouse> parentList(Warehouse warehouse);
    void assembleChildren(Warehouse warehouse);
    void assembleStockList(Warehouse warehouse);
    void assembleChildrenAndStockList(Warehouse warehouse);
    void delete(Warehouse warehouse);
    void putWarehouseIn(Warehouse warehouse);
}
