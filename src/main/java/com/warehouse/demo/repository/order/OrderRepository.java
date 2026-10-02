package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.demo.entity.order.Order;

import jakarta.transaction.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByStoreId(long id);
    boolean existsByShiftId(long id);
    boolean existsByStatusId(long id);
    boolean existsByGateId(long id);
    @Modifying(clearAutomatically = true)
    @Transactional 
    @Query("UPDATE Order o SET o.statusId = :statusId WHERE o.id = :id")
    int updateStatusById(long id, long statusId);
}
