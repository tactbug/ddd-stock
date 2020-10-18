package com.tactbug.ddd.stock.inbound.web;

import com.tactbug.ddd.stock.aggregate.valueObject.WarehouseStatusEnum;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.service.StockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(tags = "仓库管理系统controller")
@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @ApiOperation(value = "添加顶级仓库")
    @PostMapping("/stock/warehouse")
    public void createWarehouse(String name, Integer type) throws IOException {
        stockService.createWarehouse(name, type);
    }

    @ApiOperation(value = "添加子仓库")
    @PostMapping("/stock/warehouse/child")
    public void addChildWarehouse(Long parentId, Integer type) throws IOException {
        stockService.addChild(parentId, type);
    }

    @ApiOperation(value = "修改仓库名称")
    @PutMapping("/stock/warehouse/name")
    public void updateWarehouseName(Long warehouseId, String name) throws IOException {
        stockService.updateWarehouseName(warehouseId, name);
    }

    @ApiOperation(value = "移动仓库")
    @PutMapping("/stock/warehouse")
    public void moveWarehouse(Long sourceId, Long targetId) throws IOException {
        stockService.moveWarehouse(sourceId, targetId);
    }

    @ApiOperation(value = "设置仓库状态(1、启用, 2、满载, 0、禁用)")
    @PutMapping("/stock/warehouse/status")
    public void updateWarehouseStatus(Long warehouseId, Integer status) throws IOException {
        switch (status){
            case 0:{
                stockService.makeWarehouseOff(warehouseId);
                break;
            }
            case 1:{
                stockService.makeWarehouseOn(warehouseId);
                break;
            }
            case 2:{
                stockService.makeWarehouseFull(warehouseId);
                break;
            }
            default:
                throw new TactStockException("不存在的仓库状态");
        }
    }

    @ApiOperation(value = "删除仓库")
    @DeleteMapping("/stock/warehouse")
    public void deleteWarehouse(Long warehouseId) throws IOException {
        stockService.deleteWarehouse(warehouseId);
    }

    @ApiOperation(value = "管理员手动入库")
    @PostMapping("/stock/stock")
    public void putGoods(Long goodsId, Long warehouseId, Integer batch, Integer quantity) {
        stockService.putStockInByManager(goodsId, warehouseId, batch, quantity);
    }

    @ApiOperation(value = "管理员手动出库")
    @PutMapping("/stock/stock")
    public void getGoods(Long goodsId, Long warehouseId, Integer batch, Integer quantity) {
        stockService.getStockOutByManager(goodsId, warehouseId, batch, quantity);
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
