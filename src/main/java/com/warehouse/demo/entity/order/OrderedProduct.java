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
@Table(name = "ordered_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordered_products_seq")
    @SequenceGenerator(name = "ordered_products_seq", sequenceName = "ordered_products_seq", allocationSize = 50)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", referencedColumnName = "id")
    private ProductPackage productPackage;
    private BigDecimal orderedVolume;
}
