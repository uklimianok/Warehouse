package com.warehouse.demo.entity.workplace;

import java.math.BigDecimal;

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
@Table(name = "workshops")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Workshop {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workshop_seq")
    @SequenceGenerator(name = "workshop_seq", sequenceName = "workshop_seq", allocationSize = 1)
    private long id;
    private String name;
    private BigDecimal standard;
}
