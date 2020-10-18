package com.tactbug.ddd.stock.outbound.repository.stock.jpa;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "stock", indexes = {@Index(columnList = "goodsId"), @Index(columnList = "warehouseId")})
@IdClass(StockId.class)
public class StockEntity {

    @Id
    private Long goodsId;
    @Id
    private Long warehouseId;
    @Id
    private Integer batch;

    private Integer quantity;
    private Date createTime;
    private Date updateTime;
}
