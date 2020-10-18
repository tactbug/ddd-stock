package com.tactbug.ddd.stock.assist.message.event.stock;

import lombok.Data;

@Data
public class WarehouseNameUpdated {
    private String oldName;
    private String newName;
    private Integer type;
    private String typeInfo;
    private Long updatedTime;
}
