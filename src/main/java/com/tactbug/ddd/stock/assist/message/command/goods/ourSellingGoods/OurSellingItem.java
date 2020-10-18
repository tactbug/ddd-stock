package com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods;

import lombok.Data;

@Data
public class OurSellingItem {
    private Integer batch;
    private Integer quantity;
    private Long warehouseId;

    public static OurSellingItem createItem(Integer batch, Integer quantity, Long warehouseId){
        OurSellingItem ourSellingItem = new OurSellingItem();
        ourSellingItem.setBatch(batch);
        ourSellingItem.setQuantity(quantity);
        ourSellingItem.setWarehouseId(warehouseId);
        return ourSellingItem;
    }
}
