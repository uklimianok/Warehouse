package com.warehouse.demo.entity.workplace;

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
@Table(name = "gates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Gate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gate_seq")
    @SequenceGenerator(name = "gate_seq", sequenceName = "gate_seq", allocationSize = 1)
    private long id;
    private String symbol;
}
