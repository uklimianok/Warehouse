package com.warehouse.demo.entity.employee;

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
@Table(name = "shifts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Shift implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shift_seq")
    @SequenceGenerator(name = "shift_seq", sequenceName = "shift_seq", allocationSize = 1)
    private long id;
    @Column(unique = true, nullable = false)
    private String symbol; 
}
