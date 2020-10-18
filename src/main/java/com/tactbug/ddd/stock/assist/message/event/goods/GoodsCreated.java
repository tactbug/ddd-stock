package com.tactbug.ddd.stock.assist.message.event.goods;

import lombok.Data;

@Data
public class GoodsCreated {
    private Long storeId;
    private Long goodsId;
    private Integer quantity;

    public static GoodsCreated getInstance(Long storeId, Long goodsId, Integer quantity){
        GoodsCreated goodsCreated = new GoodsCreated();
        goodsCreated.setStoreId(storeId);
        goodsCreated.setGoodsId(goodsId);
        goodsCreated.setQuantity(quantity);
        return goodsCreated;
    }
}
