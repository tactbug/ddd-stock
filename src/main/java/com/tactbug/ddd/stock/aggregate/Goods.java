package com.tactbug.ddd.stock.aggregate;

import com.tactbug.ddd.stock.aggregate.root.StockRoot;
import com.tactbug.ddd.stock.assist.exception.TactStockException;
import com.tactbug.ddd.stock.assist.message.command.CallBackMessage;
import com.tactbug.ddd.stock.assist.message.command.order.ourSellingGoods.OurSellingCallBack;
import com.tactbug.ddd.stock.assist.message.command.order.ourSellingGoods.StockReduceItem;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class Goods {

    private static final Integer NO_BATCH = 0;

    private Long id;
    private List<StockRoot> stocks = new ArrayList<>();
    private Date createTime;
    private Date updateTime;

    public static Goods create(Long id){
        Goods goods = new Goods();
        goods.setId(id);
        goods.setCreateTime(new Date());
        goods.setUpdateTime(new Date());
        return goods;
    }

    public void addStock(StockRoot stockRoot){
        List<StockRoot> stockList = stocks.stream()
                .filter(s -> s.getWarehouseId().equals(stockRoot.getWarehouseId())
                        && s.getBatch().equals(stockRoot.getBatch()))
                .collect(Collectors.toList());
        if (stockList.isEmpty()){
            stocks.add(stockRoot);
        }else {
            for (StockRoot stock : stocks) {
                if (stock.getWarehouseId().equals(stockRoot.getWarehouseId())
                        && stock.getBatch().equals(stockRoot.getBatch())) {
                    stock.setQuantity(stock.getQuantity() + stockRoot.getQuantity());
                    stock.setUpdateTime(new Date());
                    break;
                }
            }
        }
        checkGoodsStock();
    }

    public void reduceStockQuantity(Long warehouseId, Integer batch, Integer quantity){
        for (StockRoot s : stocks) {
            if (s.getWarehouseId().equals(warehouseId)
                    && s.getBatch().equals(batch)) {
                if (s.getQuantity() < quantity){
                    throw new TactStockException("库存数量[" + s.getQuantity() + "]小于扣减数量[" + quantity + "]");
                }
                s.setQuantity(s.getQuantity() - quantity);
                s.setUpdateTime(new Date());
            }
            break;
        }
        checkGoodsStock();
    }

    public CallBackMessage<OurSellingCallBack> selfSelling(Integer quantity){
        int sum = stocks.stream()
                .mapToInt(StockRoot::getQuantity)
                .sum();
        if (quantity > sum){
            return CallBackMessage.error("售卖数量[" + quantity + "]大于库存数量[" + sum + "]!");
        }

        OurSellingCallBack ourSellingCallBack = new OurSellingCallBack();
        ourSellingCallBack.setGoodsId(id);

        List<Integer> batchList = stocks.stream()
                .map(StockRoot::getBatch)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, List<StockRoot>> stockMap = new HashMap<>();
        for (Integer batch : batchList) {
            List<StockRoot> list = new ArrayList<>();
            stockMap.put(batch, list);
        }
        for (StockRoot s :
                stocks) {
            stockMap.get(s.getBatch()).add(s);
        }
        Integer outBatch = outBatch(quantity, stockMap);
        if (!outBatch.equals(NO_BATCH)){
            List<StockReduceItem> itemList = outStockInBatch(outBatch, quantity);
            ourSellingCallBack.setStockItems(itemList);
            return CallBackMessage.success(ourSellingCallBack);
        }else {
            List<StockReduceItem> list = new ArrayList<>();
            Map<Integer, Integer> coordinateBatch = coordinateBatch(quantity, stockMap);
            Set<Integer> keys = coordinateBatch.keySet();
            Integer key = keys.iterator().next();
            List<Integer> removeBatch = batchList.stream()
                    .filter(i -> i < key)
                    .collect(Collectors.toList());
            for (StockRoot s :
                    stocks) {
                if (removeBatch.contains(s.getBatch())){
                    StockReduceItem item = StockReduceItem.createItem(s.getBatch(), s.getQuantity(), s.getWarehouseId());
                    s.setQuantity(0);
                    list.add(item);
                }
            }
            List<StockReduceItem> result = outStockInBatch(key, coordinateBatch.get(key));
            result.addAll(list);
            ourSellingCallBack.setStockItems(result);
            return CallBackMessage.success(ourSellingCallBack);
        }
    }

    public void sellerSelling(Integer quantity){
        if (stocks.size() != 1){
            return;
        }
        StockRoot stock = stocks.get(0);
        if (stock.getQuantity() < quantity){
            stock.setQuantity(0);
        }else {
            stock.setQuantity(stock.getQuantity() - quantity);
        }
        updateTime = new Date();
    }

    public void sellerSetQuantity(Integer quantity){
        if (stocks.size() != 1){
            return;
        }
        StockRoot stock = stocks.get(0);
        stock.setQuantity(quantity);
        updateTime = new Date();
    }

    private void checkGoodsStock(){
        long count = stocks.stream()
                .distinct()
                .count();
        if (count != stocks.size()){
            throw new TactStockException("商品[" + id + "]存在重复库存");
        }
        for (StockRoot s :
                stocks) {
            if (s.getQuantity() < 0){
                throw new TactStockException("库存商品{" + s.toString() + "}数量异常");
            }
            if (!s.getGoodsId().equals(id)){
                throw new TactStockException("库存商品ID[" + s.getGoodsId() + "]与商品ID[" + id + "]不符");
            }
        }
    }

    private List<StockReduceItem> outStockInBatch(Integer batch, Integer quantity){
        List<StockReduceItem> list = new ArrayList<>();
        int count = quantity;
        for (StockRoot stock : stocks) {
            if (stock.getBatch().equals(batch)) {
                if (stock.getQuantity() > count) {
                    stock.setQuantity(stock.getQuantity() - count);
                    stock.setUpdateTime(new Date());
                    StockReduceItem item = StockReduceItem.createItem(batch, count, stock.getWarehouseId());
                    list.add(item);
                    break;
                } else {
                    StockReduceItem item = StockReduceItem.createItem(batch, stock.getQuantity(), stock.getWarehouseId());
                    count -= stock.getQuantity();
                    stock.setQuantity(0);
                    list.add(item);
                }
            }
        }
        return list;
    }

    private Integer outBatch(Integer quantity, Map<Integer, List<StockRoot>> stockMap){
        ArrayList<Integer> batchList = new ArrayList<>(stockMap.keySet());
        batchList.sort(Comparator.comparingInt(o -> o));
        for (Integer batch :
                batchList) {
            List<StockRoot> list = stockMap.get(batch);
            int sum = list.stream()
                    .mapToInt(StockRoot::getQuantity)
                    .sum();
            if (sum > quantity){
                return batch;
            }
        }
        return NO_BATCH;
    }

    private Map<Integer, Integer> coordinateBatch(Integer quantity, Map<Integer, List<StockRoot>> stockMap){
        ArrayList<Integer> batchList = new ArrayList<>(stockMap.keySet());
        batchList.sort(Comparator.comparingInt(o -> o));
        Map<Integer, Integer> coordinate = new HashMap<>();
        int sum = 0;
        int lastSum;
        for (Integer i :
                batchList) {
            int batchSum = stockMap.get(i).stream()
                    .mapToInt(StockRoot::getQuantity)
                    .sum();
            lastSum = sum;
            sum += batchSum;
            if (sum >= quantity){
                int number = quantity - lastSum;
                coordinate.put(i, number);
                break;
            }
        }
        return coordinate;
    }
}
