package com.tactbug.ddd.stock.outbound.repository.warehouse.jpa;

import com.tactbug.ddd.stock.aggregate.root.StockRoot;
import com.tactbug.ddd.stock.aggregate.Warehouse;
import com.tactbug.ddd.stock.outbound.repository.stock.StockRepository;
import com.tactbug.ddd.stock.outbound.repository.warehouse.WarehouseRepository;
import com.tactbug.ddd.stock.aggregate.specification.WarehouseSpecification;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseStatusEnum;
import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseTypeEnum;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class WarehouseRepositoryJpaImpl implements WarehouseRepository {

    @Autowired
    private WarehouseEntityDao warehouseEntityDao;

    @Autowired
    private StockRepository stockRepository;

    @Override
    public boolean exists(Long id) {
        return warehouseEntityDao.existsById(id);
    }

    @Override
    public Warehouse getSimple(Long id) {
        WarehouseEntity warehouseEntity = warehouseEntityDao.findById(id)
                .orElseThrow(() -> new TactStockException("仓库单位获取失败"));
        Warehouse warehouse = entityToAggregate(warehouseEntity);
        WarehouseSpecification.checkWarehouse(warehouse);
        return warehouse;
    }

    @Override
    public List<Warehouse> parentList(Warehouse warehouse) {
        List<Warehouse> parentList = new ArrayList<>();
        allParentAggregateList(parentList, warehouse);
        return parentList;
    }

    @Override
    public void assembleChildren(Warehouse warehouse) {
        generateChildren(warehouse);
        WarehouseSpecification.checkWarehouse(warehouse);
    }

    @Override
    public void assembleStockList(Warehouse warehouse) {
        generateStockList(warehouse);
        WarehouseSpecification.checkWarehouse(warehouse);
    }

    @Override
    public void assembleChildrenAndStockList(Warehouse warehouse) {
        generateStockList(warehouse);
        generateChildren(warehouse);
        iterateChildren(warehouse, this::generateStockList);
        WarehouseSpecification.checkWarehouse(warehouse);
    }

    @Override
    public void delete(Warehouse warehouse) {
        if (!warehouse.getStockList().isEmpty()){
            throw new TactStockException("仓库还有库存货品未清理");
        }
        iterateChildren(warehouse, warehouse1 -> {
            if (!warehouse1.getStockList().isEmpty()){
                throw new TactStockException("仓库还有库存货品未清理");
            }
            warehouseEntityDao.delete(aggregateToEntity(warehouse1));
        });
        warehouseEntityDao.delete(aggregateToEntity(warehouse));
    }

    @Override
    public void putWarehouseIn(Warehouse warehouse) {
        List<Warehouse> list = allAggregateList(warehouse);
        List<WarehouseEntity> entityList = list.stream()
                .map(this::aggregateToEntity)
                .collect(Collectors.toList());
        warehouseEntityDao.saveAll(entityList);
    }

    private Warehouse entityToAggregate(WarehouseEntity warehouseEntity){
        Warehouse warehouse = new Warehouse();
        warehouse.setId(warehouseEntity.getId());
        warehouse.setVersion(warehouseEntity.getVersion());
        warehouse.setName(warehouseEntity.getName());
        warehouse.setWarehouseIndex(warehouseEntity.getWarehouseIndex());
        warehouse.setWarehouseType(WarehouseTypeEnum.getByType(warehouseEntity.getWarehouseType()));
        warehouse.setWarehouseStatus(WarehouseStatusEnum.getByStatus(warehouseEntity.getWarehouseStatus()));
        warehouse.setCreateTime(warehouseEntity.getCreateTime());
        warehouse.setUpdateTime(warehouseEntity.getUpdateTime());
        warehouse.setParent(warehouseEntity.getParent());
        Warehouse snapshot = new Warehouse();
        BeanUtils.copyProperties(warehouse, snapshot);
        warehouse.setSnapshot(snapshot);
        return warehouse;
    }

    private WarehouseEntity aggregateToEntity(Warehouse warehouse){
        WarehouseEntity warehouseEntity = new WarehouseEntity();
        warehouseEntity.setId(warehouse.getId());
        warehouseEntity.setVersion(warehouse.getVersion());
        warehouseEntity.setWarehouseIndex(warehouse.getWarehouseIndex());
        warehouseEntity.setName(warehouse.getName());
        warehouseEntity.setWarehouseStatus(warehouse.getWarehouseStatus().getStatus());
        warehouseEntity.setWarehouseType(warehouse.getWarehouseType().getType());
        warehouseEntity.setCreateTime(warehouse.getCreateTime());
        warehouseEntity.setUpdateTime(warehouse.getUpdateTime());
        warehouseEntity.setParent(warehouse.getParent());
        return warehouseEntity;
    }

    private void generateChildren(Warehouse warehouse){
        List<WarehouseEntity> childrenEntityList = warehouseEntityDao.findAllByParent(warehouse.getId());
        if (!childrenEntityList.isEmpty()){
            List<Warehouse> children = new ArrayList<>();
            for (WarehouseEntity w :
                    childrenEntityList) {
                Warehouse child = entityToAggregate(w);
                children.add(child);
                generateChildren(child);
            }
            warehouse.setChildren(children);
        }
    }

    private void generateStockList(Warehouse warehouse){
        List<StockRoot> stockList = stockRepository.getByWarehouse(warehouse.getId());
        warehouse.setStockList(stockList);
    }

    private void iterateChildren(Warehouse warehouse, Consumer<Warehouse> consumer){
        List<Warehouse> children = warehouse.getChildren();
        if (!children.isEmpty()){
            for (Warehouse w :
                    children) {
                consumer.accept(w);
                iterateChildren(w, consumer);
            }
        }
    }

    private List<Warehouse> allAggregateList(Warehouse root){
        List<Warehouse> list = new ArrayList<>();
        iterateChildren(root, list::add);
        if (!root.equals(root.getSnapshot())){
            list.add(root);
        }
        return list;
    }

    private void allParentAggregateList(List<Warehouse> parentList, Warehouse child){
        Optional<WarehouseEntity> optional = warehouseEntityDao.findById(child.getParent());
        if (optional.isPresent()){
            Warehouse parent = entityToAggregate(optional.get());
            parentList.add(parent);
            allParentAggregateList(parentList, parent);
        }
    }

}
