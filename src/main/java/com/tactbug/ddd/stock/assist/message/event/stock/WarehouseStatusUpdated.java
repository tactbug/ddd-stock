package com.tactbug.ddd.stock.assist.message.event.stock;

import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseStatusEnum;
import lombok.Data;

@Data
public class WarehouseStatusUpdated {
    private String name;
    private Integer type;
    private String typeInfo;
    private Integer oldStatus;
    private String oldStatusInfo;
    private Integer newStatus;
    private String newStatusInfo;
    private Long updatedTime;

    public static WarehouseStatusUpdated getSimpleInstance(WarehouseStatusEnum oldStatus, WarehouseStatusEnum newStatus){
        WarehouseStatusUpdated warehouseStatusUpdated = new WarehouseStatusUpdated();
        warehouseStatusUpdated.setOldStatus(oldStatus.getStatus());
        warehouseStatusUpdated.setOldStatusInfo(oldStatus.getMessage());
        warehouseStatusUpdated.setNewStatus(newStatus.getStatus());
        warehouseStatusUpdated.setNewStatusInfo(newStatus.getMessage());
        return warehouseStatusUpdated;
    }
}
