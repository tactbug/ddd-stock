package com.tactbug.ddd.stock.assist.message.event.order;

import lombok.Data;

@Data
public class GoodsSold {
    private Long goodsId;
    private Integer quantity;
}
