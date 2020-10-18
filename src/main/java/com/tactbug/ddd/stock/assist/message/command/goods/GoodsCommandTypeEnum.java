package com.tactbug.ddd.stock.assist.message.command.goods;

import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@AllArgsConstructor
@Getter
public enum GoodsCommandTypeEnum {
    OUR_SELLING,
    ;

    public static GoodsCommandTypeEnum getInstance(String type){
        EnumSet<GoodsCommandTypeEnum> set = EnumSet.allOf(GoodsCommandTypeEnum.class);
        for (GoodsCommandTypeEnum g :
                set) {
            if (g.toString().equals(type)){
                return g;
            }
        }
        throw new TactStockException("商品服务命令类型不存在");
    }
}
