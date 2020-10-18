package com.tactbug.ddd.stock.outbound.repository.stock.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockId implements Serializable {
    private Long goodsId;
    private Long warehouseId;
    private Integer batch;
}
