package com.warehouse.demo.entity.order;

import java.math.BigDecimal;

import com.warehouse.demo.entity.product.ProductPackage;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "picked_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PickedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "picked_products_seq")
    @SequenceGenerator(name = "picked_products_seq", sequenceName = "picked_products_seq", allocationSize = 50)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_pallet_id", referencedColumnName = "id")
    private OrderPallet orderPallet;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_package_id", referencedColumnName = "id")
    private ProductPackage productPackage;
    private BigDecimal pickedVolume;
}
