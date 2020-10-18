package com.tactbug.ddd.stock.outbound.repository.goods.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsEntityDao extends JpaRepository<GoodsEntity, Long> {
}
