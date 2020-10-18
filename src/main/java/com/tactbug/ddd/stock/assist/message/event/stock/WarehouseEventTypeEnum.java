package com.tactbug.ddd.stock.assist.message.event.stock;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum WarehouseEventTypeEnum{
    WAREHOUSE_CREATED, CHILD_ADDED, WAREHOUSE_NAME_UPDATED, WAREHOUSE_MOVED, WAREHOUSE_FULL,
    WAREHOUSE_OFF, WAREHOUSE_ACTIVE, WAREHOUSE_DELETED;

    public static WarehouseEventTypeEnum getWarehouseType(String type){
        EnumSet<WarehouseEventTypeEnum> set = EnumSet.allOf(WarehouseEventTypeEnum.class);
        for (WarehouseEventTypeEnum w :
                set) {
            if (type.equals(w.toString())){
                return w;
            }
        }
        throw new TactStockException("仓库事件变更类型不存在");
    }
}
