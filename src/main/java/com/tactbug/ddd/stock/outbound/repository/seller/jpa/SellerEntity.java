package com.tactbug.ddd.stock.outbound.repository.seller.jpa;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "seller")
public class SellerEntity {

    @Id
    private Long id;

    private Long areaId;

    private Date createTime;
    private Date updateTime;
}
