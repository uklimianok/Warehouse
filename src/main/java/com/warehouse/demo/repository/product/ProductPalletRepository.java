package com.warehouse.demo.repository.product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.demo.entity.product.ProductPallet;
import com.warehouse.demo.util.info.StatusInfo;

public interface ProductPalletRepository extends JpaRepository<ProductPallet, Long> {
    boolean existsByProductPackageId(long id);
    boolean existsByPalletId(long id);
    boolean existsByStatusId(long id);
    boolean existsByWorkStationId(long id);
    boolean existsByPalletNumber(String palletNumber);
    boolean existsByNextWorkStationId(long id);
    Optional<ProductPallet> findByPalletNumber(String palletNumber);
    @Transactional 
    @Modifying  // Allows manual UPDATE/DELETE @Query
    @Query(
        nativeQuery = true, 
        value = "UPDATE product_pallets SET next_work_station_id = :newValue WHERE id = :id AND status_id = (SELECT id FROM statuses WHERE name = '" + StatusInfo.PRODUCT_PALLET_UNLOADED + "' AND type = 'Product pallet') AND next_work_station_id IS NOT DISTINCT FROM :expectedOldValue"
    )   // non-static final value cannot be invoked in an annotation
    int updateNextWorkStationIfMatching(@Param("id") long id, @Param("newValue") Long newNextWorkStationId, @Param("expectedOldValue") Long oldNextWorkStationId);
}
