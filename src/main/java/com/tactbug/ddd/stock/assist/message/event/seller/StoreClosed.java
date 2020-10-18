package com.tactbug.ddd.stock.assist.message.event.seller;

import lombok.Data;

@Data
public class StoreClosed {
    private Long sellerId;
}
