package com.tactbug.ddd.stock.aggregate;

import com.google.common.base.Objects;
import com.tactbug.ddd.stock.aggregate.function.IterateChildren;
import com.tactbug.ddd.stock.aggregate.root.StockRoot;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseStatusEnum;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseTypeEnum;
import com.tactbug.ddd.stock.assist.message.event.EventMessage;
import com.tactbug.ddd.stock.assist.message.event.stock.*;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Warehouse {

    private Long id;
    private Integer version;

    private String name;
    private Integer warehouseIndex;
    private WarehouseTypeEnum warehouseType;
    private WarehouseStatusEnum warehouseStatus;
    private List<StockRoot> stockList = new ArrayList<>();
    private List<Warehouse> children = new ArrayList<>();
    private Long parent;

    private Date createTime;
    private Date updateTime;

    private Warehouse snapshot;

    public EventMessage<WarehouseEventTypeEnum, WarehouseCreated> warehouseCreated(){
        WarehouseCreated warehouseCreated = new WarehouseCreated();
        warehouseCreated.setName(name);
        warehouseCreated.setType(warehouseType.getType());
        warehouseCreated.setTypeInfo(warehouseType.getMessage());
        warehouseCreated.setCreatedTime(System.currentTimeMillis());
        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_CREATED, warehouseCreated);
    }

    public EventMessage<WarehouseEventTypeEnum, ChildAdded> addChild(Warehouse child){
        children.add(child);
        ChildAdded childAdded = new ChildAdded();
        childAdded.setChildName(child.getName());
        childAdded.setChildIndex(child.getWarehouseIndex());
        childAdded.setChildType(child.getWarehouseType().getType());
        childAdded.setChildTypeInfo(child.getWarehouseType().getMessage());
        childAdded.setChildId(child.getId());
        childAdded.setAddedTime(System.currentTimeMillis());
        return new EventMessage<>(child.getId(), WarehouseEventTypeEnum.CHILD_ADDED, childAdded);
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseNameUpdated> updateName(String name){

        WarehouseNameUpdated warehouseNameUpdated = new WarehouseNameUpdated();
        warehouseNameUpdated.setOldName(this.name);
        warehouseNameUpdated.setNewName(name);
        warehouseNameUpdated.setType(warehouseType.getType());
        warehouseNameUpdated.setTypeInfo(warehouseType.getMessage());
        warehouseNameUpdated.setUpdatedTime(System.currentTimeMillis());

        this.name = name;
        iterateChildren(this, (parent, target) -> {
            target.setName(parent.getName() + "第" + target.getWarehouseIndex() + target.getWarehouseType().getMessage());
            sync(target);
        });
        sync(this);

        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_NAME_UPDATED, warehouseNameUpdated);
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseMoved> acceptChild(Warehouse child){

        WarehouseMoved warehouseMoved = new WarehouseMoved();
        warehouseMoved.setName(child.getName());
        warehouseMoved.setFrom(child.getParent());
        warehouseMoved.setTo(id);
        warehouseMoved.setParentName(name);
        warehouseMoved.setType(child.getWarehouseType().getType());
        warehouseMoved.setTypeInfo(child.getWarehouseType().getMessage());
        warehouseMoved.setMovedTime(System.currentTimeMillis());

        if (children.isEmpty()){
            child.setWarehouseIndex(1);
        }else {
            List<Integer> sameTypeList = children.stream()
                    .filter(c -> c.getWarehouseType().equals(child.warehouseType))
                    .map(Warehouse::getWarehouseIndex)
                    .sorted()
                    .collect(Collectors.toList());
            if (sameTypeList.isEmpty()){
                child.setWarehouseIndex(1);
            }else {
                int index = 1;
                for (Integer i :
                        sameTypeList) {
                    if (i.equals(index)){
                        index += 1;
                    }else {
                        break;
                    }
                }
                child.setWarehouseIndex(index);
            }
        }
        child.updateName(name + "第" + child.getWarehouseIndex() + child.getWarehouseType().getMessage());
        child.setParent(this.id);
        children.add(child);

        return new EventMessage<>(child.getId(), WarehouseEventTypeEnum.WAREHOUSE_MOVED, warehouseMoved);
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> makeFull(){
        WarehouseStatusUpdated warehouseStatusUpdated =
                WarehouseStatusUpdated.getSimpleInstance(warehouseStatus, WarehouseStatusEnum.ENOUGH);
        warehouseStatusUpdated.setName(name);
        warehouseStatusUpdated.setType(warehouseType.getType());
        warehouseStatusUpdated.setTypeInfo(warehouseType.getMessage());
        warehouseStatusUpdated.setUpdatedTime(System.currentTimeMillis());

        if (stockList.isEmpty()){
            throw new TactStockException("仓库库存为空");
        }
        warehouseStatus = WarehouseStatusEnum.ENOUGH;
        sync(this);

        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_FULL, warehouseStatusUpdated);
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> makeOff(){
        WarehouseStatusUpdated warehouseStatusUpdated =
                WarehouseStatusUpdated.getSimpleInstance(warehouseStatus, WarehouseStatusEnum.OFF);
        warehouseStatusUpdated.setName(name);
        warehouseStatusUpdated.setType(warehouseType.getType());
        warehouseStatusUpdated.setTypeInfo(warehouseType.getMessage());
        warehouseStatusUpdated.setUpdatedTime(System.currentTimeMillis());

        if (!stockList.isEmpty()){
            throw new TactStockException("仓库里还有库存商品未清空");
        }
        iterateChildren(this, (parent, target) -> {
            if (!target.getStockList().isEmpty()){
                throw new TactStockException("仓库里还有库存商品未清空");
            }
            target.setWarehouseStatus(WarehouseStatusEnum.OFF);
            sync(target);
        });
        warehouseStatus = WarehouseStatusEnum.OFF;
        sync(this);

        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_OFF, warehouseStatusUpdated);
    }

    public EventMessage<WarehouseEventTypeEnum, WarehouseStatusUpdated> makeActive(){
        WarehouseStatusUpdated warehouseStatusUpdated =
                WarehouseStatusUpdated.getSimpleInstance(warehouseStatus, WarehouseStatusEnum.ACTIVE);
        warehouseStatusUpdated.setName(name);
        warehouseStatusUpdated.setType(warehouseType.getType());
        warehouseStatusUpdated.setTypeInfo(warehouseType.getMessage());
        warehouseStatusUpdated.setUpdatedTime(System.currentTimeMillis());

        iterateChildren(this, (parent, target) -> {
            target.setWarehouseStatus(WarehouseStatusEnum.ACTIVE);
            sync(target);
        });
        warehouseStatus = WarehouseStatusEnum.ACTIVE;
        sync(this);

        return new EventMessage<>(id, WarehouseEventTypeEnum.WAREHOUSE_ACTIVE, warehouseStatusUpdated);
    }

    private void iterateChildren(Warehouse root, IterateChildren iterate){
        if (!root.children.isEmpty()){
            for (Warehouse w :
                    root.children) {
                iterate.accept(root, w);
                iterateChildren(w, iterate);
            }
        }
    }

    private void sync(Warehouse warehouse){
        if (!warehouse.equals(snapshot)){
            warehouse.setUpdateTime(new Date());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Objects.equal(id, warehouse.id) &&
                Objects.equal(name, warehouse.name) &&
                Objects.equal(warehouseIndex, warehouse.warehouseIndex) &&
                warehouseType == warehouse.warehouseType &&
                warehouseStatus == warehouse.warehouseStatus &&
                Objects.equal(parent, warehouse.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, warehouseIndex, warehouseType, warehouseStatus, parent);
    }
}
