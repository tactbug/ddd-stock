package com.tactbug.ddd.stock.outbound.repository.seller;

import com.tactbug.ddd.stock.aggregate.Seller;

public interface SellerRepository {
    boolean exists(Long id);
    Seller getById(Long id);
    void putSellerIn(Seller seller);
    void delete(Seller seller);
}
