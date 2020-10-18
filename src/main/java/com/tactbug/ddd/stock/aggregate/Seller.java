package com.tactbug.ddd.stock.aggregate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seller {

    private Long id;
    private Long areaId;

    private Date createTime;
    private Date updateTime;

    public void addArea(Long areaId){
        this.areaId = areaId;
        this.updateTime = new Date();
    }

}
