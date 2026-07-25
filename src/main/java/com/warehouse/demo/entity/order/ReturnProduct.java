package com.warehouse.demo.entity.order;

import com.warehouse.demo.entity.product.Product;

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
@Table(name = "return_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReturnProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "return_products_seq")
    @SequenceGenerator(name = "return_products_seq", sequenceName = "return_products_seq", allocationSize = 50)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    private int productsAmount;
}
