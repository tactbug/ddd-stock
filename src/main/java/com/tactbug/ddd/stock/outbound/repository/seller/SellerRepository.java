package com.tactbug.ddd.stock.outbound.repository.seller;

import com.tactbug.ddd.stock.aggregate.Seller;

public interface SellerRepository {
    boolean exists(Long id);
    Seller getById(Long id);
    void putStoreIn(Seller seller);
    void delete(Seller seller);
}
