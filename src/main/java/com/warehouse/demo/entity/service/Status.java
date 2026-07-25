package com.warehouse.demo.entity.service;

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
@Table(name = "statuses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "status_seq")
    @SequenceGenerator(name = "status_seq", sequenceName = "status_seq", allocationSize = 1)
    private long id;
    private String name;
}
