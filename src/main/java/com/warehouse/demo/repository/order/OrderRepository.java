package com.warehouse.demo.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.warehouse.demo.entity.order.Order;
import com.warehouse.demo.entity.service.Status;

import jakarta.transaction.Transactional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByStoreId(long storeId);
    boolean existsByShiftId(long shiftId);
    boolean existsByStatusId(long statusId);
    boolean existsByGateId(long gateId);
    @Modifying(clearAutomatically = true)
    @Transactional 
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
    int updateStatusById(long id, Status status);
}
