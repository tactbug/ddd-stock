package com.tactbug.ddd.stock.assist.message.event.goods;

import lombok.Data;

@Data
public class GoodsSold {
    private Long goodsId;
    private Integer quantity;
}
