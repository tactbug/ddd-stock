package com.tactbug.ddd.stock.assist.message.event.stock;

import lombok.Data;

@Data
public class WarehouseDeleted {
    private String name;
    private Integer type;
    private String typeInfo;
    private Integer status;
    private String statusInfo;
    private Long deleteTime;
}
