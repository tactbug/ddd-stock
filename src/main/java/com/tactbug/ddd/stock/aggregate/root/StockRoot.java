package com.tactbug.ddd.stock.aggregate.root;

import com.google.common.base.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class StockRoot {

    private Long goodsId;
    private Long warehouseId;
    private Integer batch;
    private Integer quantity;

    private Date createTime;
    private Date updateTime;

    private StockRoot snapshot;

    public StockRoot(Long goodsId, Long warehouseId, Integer batch, Integer quantity) {
        this.goodsId = goodsId;
        this.warehouseId = warehouseId;
        this.batch = batch;
        this.quantity = quantity;
        createTime = new Date();
        updateTime = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockRoot stock = (StockRoot) o;
        return Objects.equal(goodsId, stock.goodsId) &&
                Objects.equal(warehouseId, stock.warehouseId) &&
                Objects.equal(batch, stock.batch);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(goodsId, warehouseId, batch);
    }
}
