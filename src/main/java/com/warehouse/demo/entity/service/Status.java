package com.warehouse.demo.entity.service;

import com.warehouse.demo.entity.Identifiable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "statuses", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "type"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Status implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_seq")
    @SequenceGenerator(name = "status_seq", sequenceName = "status_seq", allocationSize = 1)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String type;
}
