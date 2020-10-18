package com.tactbug.ddd.stock.outbound.repository.warehouse.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseEntityDao extends JpaRepository<WarehouseEntity, Long> {
    List<WarehouseEntity> findAllByParent(Long id);
}
