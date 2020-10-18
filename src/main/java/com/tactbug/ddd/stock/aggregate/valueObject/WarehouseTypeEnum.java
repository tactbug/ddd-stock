package com.tactbug.ddd.stock.aggregate.valueObject;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum WarehouseTypeEnum {
    WAREHOUSE(1, "仓库"),
    AREA(2, "区域"),
    SHELVE(3, "货架"),
    POSITION(4, "货位"),
    ;

    private final Integer type;
    private final String message;

    public static WarehouseTypeEnum getByType(Integer type){
        EnumSet<WarehouseTypeEnum> set = EnumSet.allOf(WarehouseTypeEnum.class);
        for (WarehouseTypeEnum w :
                set) {
            if (w.getType().equals(type)){
                return w;
            }
        }
        throw new TactStockException("仓库类型不存在");
    }
}
