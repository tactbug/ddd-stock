package com.tactbug.ddd.stock.assist.message.event.order;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum OrderEventTypeEnum {
    SELLER_SELLING,
    ;

    public static OrderEventTypeEnum getType(String type){
        EnumSet<OrderEventTypeEnum> set = EnumSet.allOf(OrderEventTypeEnum.class);
        for (OrderEventTypeEnum g :
                set) {
            if (g.toString().equals(type)){
                return g;
            }
        }
        throw new TactStockException("订单服务广播类型不存在");
    }
}
