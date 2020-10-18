package com.tactbug.ddd.stock.outbound.repository.seller.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerEntityDao extends JpaRepository<SellerEntity, Long> {

}
