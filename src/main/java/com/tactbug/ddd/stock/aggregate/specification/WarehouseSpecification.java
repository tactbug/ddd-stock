package com.tactbug.ddd.stock.aggregate.specification;

import com.google.common.base.Strings;
import com.tactbug.ddd.stock.aggregate.Warehouse;
import com.tactbug.ddd.stock.aggregate.root.StockRoot;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseStatusEnum;
import com.tactbug.ddd.stock.assist.exception.TactStockException;


public class WarehouseSpecification {

    public static void checkWarehouse(Warehouse warehouse) {
        checkNullValue(warehouse);
        if (!warehouse.getStockList().isEmpty()){
            checkStockList(warehouse);
        }
        if (!warehouse.getChildren().isEmpty()){
            checkChildren(warehouse);
        }
        checkStatus(warehouse);
    }

    private static void checkNullValue(Warehouse warehouse){
        if (null == warehouse.getId()){
            throw new TactStockException("仓库ID不能为空");
        }
        if (null == warehouse.getWarehouseIndex()){
            throw new TactStockException("仓库序号不能为空");
        }
        if (null == warehouse.getWarehouseType()){
            throw new TactStockException("仓库类型不能为空");
        }
        if (Strings.isNullOrEmpty(warehouse.getName())){
            throw new TactStockException("仓库名称不能为空");
        }
        if (null == warehouse.getWarehouseStatus()){
            throw new TactStockException("仓库状态不能为空");
        }
    }

    private static void checkStockList(Warehouse warehouse){
        for (StockRoot s :
                warehouse.getStockList()) {
            if (!s.getWarehouseId().equals(warehouse.getId())){
                throw new TactStockException("库存商品存储位置错误");
            }
        }
    }

    private static void checkChildren(Warehouse warehouse){
        String parentName = warehouse.getName();
        int nameLength = parentName.length();
        for (Warehouse w :
                warehouse.getChildren()) {
            String preName = w.getName().substring(0, nameLength);
            if (!preName.equals(parentName)){
                throw new TactStockException("子仓库所属上级仓库错误");
            }
        }
    }

    private static void checkStatus(Warehouse warehouse){
        switch (warehouse.getWarehouseStatus()){
            case OFF: {
                for (Warehouse w :
                        warehouse.getChildren()) {
                    if (!w.getWarehouseStatus().equals(WarehouseStatusEnum.OFF)){
                        throw new TactStockException("子仓库单位还有未禁用的");
                    }
                }
                break;
            }
            case ENOUGH:{
                if (warehouse.getStockList().isEmpty()){
                    throw new TactStockException("当前仓库无库存");
                }
                break;
            }
            case ACTIVE:{
                break;
            }
            default:
                throw new TactStockException("当前仓库状态不存在");
        }
    }
}
