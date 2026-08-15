package com.warehouse.demo.entity.product;

import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.entity.service.Status;
import com.warehouse.demo.entity.workplace.WorkStation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_pallets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductPallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_pallet_seq")
    @SequenceGenerator(name = "product_pallet_seq", sequenceName = "product_pallet_seq", allocationSize = 50)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_package_id", referencedColumnName = "id")
    private ProductPackage productPackage;
    private int packageAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pallet_id", referencedColumnName = "id")
    private Pallet pallet;
    @Column(unique = true)
    private String palletNumber;
    private String groupNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status status;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="work_station_id", referencedColumnName = "id")
    private WorkStation workStation;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="next_work_station_id", referencedColumnName = "id")
    private WorkStation nextWorkStation;
}
