package com.tactbug.ddd.stock.assist.message.event.stock;

import lombok.Data;


@Data
public class WarehouseCreated {

    private String name;
    private Integer type;
    private String typeInfo;
    private Long createdTime;

}
