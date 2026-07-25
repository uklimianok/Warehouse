package com.warehouse.demo.entity.product;

import java.math.BigDecimal;

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
@Table(name="packages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductPackage {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="package_seq")
    @SequenceGenerator(name="package_seq", sequenceName="package_seq", allocationSize=50)
    private long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="product_id", referencedColumnName="id")
    private Product product;
    private int productsAmount;
    private BigDecimal volume;
    private BigDecimal weight;
}
