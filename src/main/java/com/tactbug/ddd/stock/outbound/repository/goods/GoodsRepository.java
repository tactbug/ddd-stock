package com.tactbug.ddd.stock.outbound.repository.goods;

import com.tactbug.ddd.stock.aggregate.Goods;

public interface GoodsRepository {
    boolean exists(Long id);
    Goods getById(Long id);
    void putGoodsIn(Goods goods);
    void delete(Goods goods);
}
