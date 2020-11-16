package com.tactbug.ddd.stock.assist.message.command.order.ourSellingGoods;

import lombok.Data;

@Data
public class StockReduceItem {
    private Integer batch;
    private Integer quantity;
    private Long warehouseId;

    public static StockReduceItem createItem(Integer batch, Integer quantity, Long warehouseId){
        StockReduceItem ourSellingItem = new StockReduceItem();
        ourSellingItem.setBatch(batch);
        ourSellingItem.setQuantity(quantity);
        ourSellingItem.setWarehouseId(warehouseId);
        return ourSellingItem;
    }
}
