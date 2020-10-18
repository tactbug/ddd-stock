package com.tactbug.ddd.stock.aggregate.factory;

import com.tactbug.ddd.stock.aggregate.Warehouse;
import com.tactbug.ddd.stock.aggregate.specification.WarehouseSpecification;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseStatusEnum;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseTypeEnum;
import com.tactbug.ddd.stock.assist.utils.CodeUtil;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class WarehouseFactory {

    private static final Integer TOP_INDEX = 999;
    private static final Long ROOT_PARENT = 0L;
    private static final Integer START_VERSION = 1;

    public static Warehouse createTopWarehouse(String name, Integer warehouseType){

        Warehouse warehouse = new Warehouse();
        warehouse.setId(CodeUtil.nextId());
        warehouse.setVersion(START_VERSION);
        warehouse.setWarehouseIndex(TOP_INDEX);
        warehouse.setName(name);
        warehouse.setWarehouseType(WarehouseTypeEnum.getByType(warehouseType));
        warehouse.setWarehouseStatus(WarehouseStatusEnum.ACTIVE);
        warehouse.setCreateTime(new Date());
        warehouse.setUpdateTime(new Date());
        warehouse.setParent(ROOT_PARENT);
        WarehouseSpecification.checkWarehouse(warehouse);
        return warehouse;

    }

    public static Warehouse createChild(Warehouse warehouse, Integer warehouseType){
        List<Warehouse> children = warehouse.getChildren();
        int index = 1;
        long indexCount = children.stream()
                .filter(c -> c.getWarehouseType().getType().equals(warehouseType))
                .count();
        if (indexCount > 0){
            List<Integer> indexArray = children.stream()
                    .filter(c -> c.getWarehouseType().getType().equals(warehouseType))
                    .map(Warehouse::getWarehouseIndex)
                    .sorted()
                    .collect(Collectors.toList());
            for (Integer integer : indexArray) {
                if (index == integer) {
                    index += 1;
                }else {
                    break;
                }
            }
        }
        Warehouse child = new Warehouse();
        child.setId(CodeUtil.nextId());
        child.setVersion(START_VERSION);
        child.setWarehouseIndex(index);
        child.setWarehouseType(WarehouseTypeEnum.getByType(warehouseType));
        child.setName(warehouse.getName() + "第" + child.getWarehouseIndex() + child.getWarehouseType().getMessage());
        child.setWarehouseStatus(WarehouseStatusEnum.ACTIVE);
        child.setParent(warehouse.getId());
        child.setCreateTime(new Date());
        child.setUpdateTime(new Date());
        WarehouseSpecification.checkWarehouse(child);
        return child;
    }
}
