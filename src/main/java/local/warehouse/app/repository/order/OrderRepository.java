package local.warehouse.app.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import local.warehouse.app.entity.order.Order;
import local.warehouse.app.entity.service.Status;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByStoreId(long storeId);

    boolean existsByShiftId(long shiftId);

    boolean existsByStatusId(long statusId);

    boolean existsByGateId(long gateId);
    
    @Modifying
    @Transactional 
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
    int updateStatusById(long id, Status status);
}
