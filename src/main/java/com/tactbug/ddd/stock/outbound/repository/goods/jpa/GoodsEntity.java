package com.tactbug.ddd.stock.outbound.repository.goods.jpa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "goods")
public class GoodsEntity {
    @Id
    private Long id;

    private Date createTime;
    private Date updateTime;
}
