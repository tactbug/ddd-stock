package com.tactbug.ddd.stock.assist.message.event.stock;

import lombok.Data;

@Data
public class ChildAdded {
    
    private Long childId;
    private String childName;
    private Integer childIndex;
    private Integer childType;
    private String childTypeInfo;
    private Long addedTime;
    
}
