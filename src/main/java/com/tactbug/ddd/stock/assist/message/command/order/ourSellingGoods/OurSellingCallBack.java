package com.tactbug.ddd.stock.assist.message.command.order.ourSellingGoods;

import lombok.Data;

import java.util.List;

@Data
public class OurSellingCallBack {
    private Long goodsId;
    private List<StockReduceItem> stockItems;
}
