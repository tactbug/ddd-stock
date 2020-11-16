package com.tactbug.ddd.stock.outbound.repository.stock.jpa;

import com.tactbug.ddd.stock.outbound.repository.stock.StockRepository;
import com.tactbug.ddd.stock.aggregate.root.StockRoot;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StockRepositoryJpaImpl implements StockRepository {

    @Autowired
    private StockEntityDao stockEntityDao;

    @Override
    public void putStockIn(List<StockRoot> stockList) {
        for (StockRoot s :
                stockList) {
            if (!s.equals(s.getSnapshot()) || !s.getQuantity().equals(s.getSnapshot().getQuantity())){
                if (s.getQuantity() <= 0){
                    stockEntityDao.delete(rootToEntity(s));
                }else {
                    stockEntityDao.save(rootToEntity(s));
                }
            }
        }
    }

    @Override
    public void delete(List<StockRoot> stockList) {
        List<StockEntity> entityList = stockList.stream()
                .map(this::rootToEntity)
                .collect(Collectors.toList());
        stockEntityDao.deleteAll(entityList);
    }

    @Override
    public List<StockRoot> getByGoods(Long goodsId) {
        return stockEntityDao.findAllByGoodsId(goodsId).stream()
                .map(this::entityToRoot)
                .collect(Collectors.toList());
    }

    @Override
    public List<StockRoot> getByWarehouse(Long warehouse) {
        return stockEntityDao.findAllByWarehouseId(warehouse).stream()
                .map(this::entityToRoot)
                .collect(Collectors.toList());
    }

    private StockRoot entityToRoot(StockEntity stockEntity){

        StockRoot stock = new StockRoot();
        stock.setGoodsId(stockEntity.getGoodsId());
        stock.setWarehouseId(stockEntity.getWarehouseId());
        stock.setBatch(stockEntity.getBatch());
        stock.setQuantity(stockEntity.getQuantity());
        stock.setCreateTime(stockEntity.getCreateTime());
        stock.setUpdateTime(stockEntity.getUpdateTime());

        StockRoot snapshot = new StockRoot();
        BeanUtils.copyProperties(stock, snapshot);
        stock.setSnapshot(snapshot);

        return stock;
    }

    private StockEntity rootToEntity(StockRoot stock){
        StockEntity stockEntity = new StockEntity();
        stockEntity.setGoodsId(stock.getGoodsId());
        stockEntity.setWarehouseId(stock.getWarehouseId());
        stockEntity.setBatch(stock.getBatch());
        stockEntity.setQuantity(stock.getQuantity());
        stockEntity.setCreateTime(stock.getCreateTime());
        stockEntity.setUpdateTime(stock.getUpdateTime());

        return stockEntity;
    }
}
