package com.tactbug.ddd.stock.assist.message.command.goods.ourSellingGoods;

import lombok.Data;

@Data
public class OurSellingCommand {
    private Long goodsId;
    private Integer quantity;

    public static OurSellingCommand getInstance(Long goodsId, Integer quantity){
        OurSellingCommand ourSellingCommand = new OurSellingCommand();
        ourSellingCommand.setGoodsId(goodsId);
        ourSellingCommand.setQuantity(quantity);
        return ourSellingCommand;
    }
}
