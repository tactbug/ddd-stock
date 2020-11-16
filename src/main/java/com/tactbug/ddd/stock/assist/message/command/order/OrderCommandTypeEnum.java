package com.tactbug.ddd.stock.assist.message.command.order;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum OrderCommandTypeEnum {
    OUR_SELLING,
    ;

    public static OrderCommandTypeEnum getInstance(String type){
        EnumSet<OrderCommandTypeEnum> set = EnumSet.allOf(OrderCommandTypeEnum.class);
        for (OrderCommandTypeEnum g :
                set) {
            if (g.toString().equals(type)){
                return g;
            }
        }
        throw new TactStockException("商品服务命令类型不存在");
    }
}
