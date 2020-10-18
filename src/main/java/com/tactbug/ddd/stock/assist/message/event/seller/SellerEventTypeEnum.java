package com.tactbug.ddd.stock.assist.message.event.seller;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum SellerEventTypeEnum {
    CLOSE_STORE, OPEN_SHOP,
    ;

    public static SellerEventTypeEnum getType(String key){
        EnumSet<SellerEventTypeEnum> set = EnumSet.allOf(SellerEventTypeEnum.class);
        for (SellerEventTypeEnum s :
                set) {
            if (s.toString().equals(key)){
                return s;
            }
        }
        throw new TactStockException("卖家服务事件不存在");
    }
}
