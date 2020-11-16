package com.tactbug.ddd.stock.aggregate;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Seller {

    private Long id;
    private Long areaId;

    private Date createTime;
    private Date updateTime;

    public Seller(Long id, Long areaId){
        this.id = id;
        this.areaId = areaId;
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
