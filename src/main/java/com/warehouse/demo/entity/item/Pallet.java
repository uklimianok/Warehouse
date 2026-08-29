package com.warehouse.demo.entity.item;

import java.math.BigDecimal;

import com.warehouse.demo.entity.Identifiable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pallets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Pallet implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pallet_seq")
    @SequenceGenerator(name = "pallet_seq", sequenceName = "pallet_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private BigDecimal length;
    @Column(nullable = false)
    private BigDecimal width;
    @Column(nullable = false)
    private BigDecimal height;
    @Column(nullable = false)
    private BigDecimal weight;
}
