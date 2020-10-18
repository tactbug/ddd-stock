package com.tactbug.ddd.stock.outbound.repository.warehouse.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;

@Data
@Entity
@Table(name = "warehouse")
public class WarehouseEntity {

    @Id
    private Long id;
    @Version
    private Integer version;

    private String name;
    private Integer warehouseIndex;
    private Integer warehouseType;
    private Integer warehouseStatus;
    private Long parent;

    private Date createTime;
    private Date updateTime;
}
