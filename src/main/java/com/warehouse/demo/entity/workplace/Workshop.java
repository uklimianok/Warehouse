package com.warehouse.demo.entity.workplace;

import java.math.BigDecimal;

import com.warehouse.demo.entity.Identifiable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "workshops")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Workshop implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workshop_seq")
    @SequenceGenerator(name = "workshop_seq", sequenceName = "workshop_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal standard;
    @Version 
    private long version;   // For DB safe I/O operations and restore lastAssignedOperatorIndex
    @Column(nullable = false)
    private int lastAssignedOperatorIndex = 0;
}
