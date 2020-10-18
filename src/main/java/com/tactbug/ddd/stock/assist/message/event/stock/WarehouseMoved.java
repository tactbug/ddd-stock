package com.tactbug.ddd.stock.assist.message.event.stock;

import lombok.Data;

@Data
public class WarehouseMoved {
    
    private String name;
    private Integer type;
    private String typeInfo;
    private Long from;
    private Long to;
    private String parentName;
    private Long movedTime;
    
}
