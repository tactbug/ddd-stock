package com.tactbug.ddd.stock.aggregate.valueObject;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum WarehouseStatusEnum {
    ACTIVE(1, "可以使用"),
    ENOUGH(2, "此仓储成员已满"),
    OFF(0, "停止使用"),
    ;

    private final Integer status;
    private final String message;

    public static WarehouseStatusEnum getByStatus(Integer status){
        EnumSet<WarehouseStatusEnum> set = EnumSet.allOf(WarehouseStatusEnum.class);
        for (WarehouseStatusEnum w :
                set) {
            if (w.getStatus().equals(status)){
                return w;
            }
        }
        throw new TactStockException("仓库状态不存在");
    }
}
