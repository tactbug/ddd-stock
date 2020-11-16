package com.tactbug.ddd.stock.outbound.repository.goods.jpa;

import com.tactbug.ddd.stock.aggregate.Goods;
import com.tactbug.ddd.stock.aggregate.root.StockRoot;
import com.tactbug.ddd.stock.outbound.repository.goods.GoodsRepository;
import com.tactbug.ddd.stock.outbound.repository.stock.StockRepository;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class GoodsRepositoryJpaImpl implements GoodsRepository {

    @Autowired
    private GoodsEntityDao goodsEntityDao;

    @Autowired
    private StockRepository stockRepository;

    @Override
    public boolean exists(Long id) {
        return goodsEntityDao.existsById(id);
    }

    @Override
    public Goods getById(Long id) {
        GoodsEntity goodsEntity = goodsEntityDao.findById(id).orElseThrow(() -> new TactStockException("商品不存在"));
        Goods goods = entityToAggregate(goodsEntity);
        List<StockRoot> stockRootList = stockRepository.getByGoods(id);
        goods.setStocks(stockRootList);
        return goods;
    }

    @Override
    public void putGoodsIn(Goods goods) {

        List<StockRoot> stocks = goods.getStocks();
        stockRepository.putStockIn(stocks);

        int sum = goods.getStocks().stream()
                .mapToInt(StockRoot::getQuantity)
                .sum();
        if (sum == 0){
            goodsEntityDao.delete(aggregateToEntity(goods));
        }else {
            goodsEntityDao.save(aggregateToEntity(goods));
        }
    }

    @Override
    public void delete(Goods goods) {
        if (goodsEntityDao.existsById(goods.getId())){
            stockRepository.delete(goods.getStocks());
            goodsEntityDao.delete(aggregateToEntity(goods));
        }
    }

    private Goods entityToAggregate(GoodsEntity goodsEntity){
        Goods goods = new Goods();
        goods.setId(goodsEntity.getId());
        goods.setCreateTime(goodsEntity.getCreateTime());
        goods.setUpdateTime(goodsEntity.getUpdateTime());
        return goods;
    }

    private GoodsEntity aggregateToEntity(Goods goods){
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setId(goods.getId());
        goodsEntity.setCreateTime(new Date());
        goodsEntity.setUpdateTime(new Date());
        return goodsEntity;
    }
}
