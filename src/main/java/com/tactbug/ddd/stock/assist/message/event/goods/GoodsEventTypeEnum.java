package com.tactbug.ddd.stock.assist.message.event.goods;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum GoodsEventTypeEnum {
    SELLER_BAN_GOODS, SELLER_CREATE_GOODS,
    SELLER_UPDATE_QUANTITY,
    ;

    public static GoodsEventTypeEnum getType(String type){
        EnumSet<GoodsEventTypeEnum> set = EnumSet.allOf(GoodsEventTypeEnum.class);
        for (GoodsEventTypeEnum g :
                set) {
            if (g.toString().equals(type)){
                return g;
            }
        }
        throw new TactStockException("商品服务广播类型不存在");
    }
}
