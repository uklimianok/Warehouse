package com.warehouse.demo.entity.product;

import com.warehouse.demo.entity.employee.organization.Organization;

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
@Table(name="products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="products_seq")
    @SequenceGenerator(name="products_seq", sequenceName="products_seq", allocationSize=50)
    private long id;
    private String name;
    private String barcode_number;
    private double cost;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="producer_id", referencedColumnName="id")
    private Organization producer;
}
