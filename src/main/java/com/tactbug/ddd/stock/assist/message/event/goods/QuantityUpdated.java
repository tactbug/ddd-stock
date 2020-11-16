package com.tactbug.ddd.stock.assist.message.event.goods;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuantityUpdated {
    private Long sellerId;
    private Long goodsId;
    private Integer quantity;
}
