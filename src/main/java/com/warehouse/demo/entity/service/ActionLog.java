package com.warehouse.demo.entity.service;

import java.time.LocalDateTime;

import com.warehouse.demo.entity.Identifiable;
import com.warehouse.demo.entity.employee.Employee;

import jakarta.persistence.Column;
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
@Table(name = "action_logs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ActionLog implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "action_logs_seq")
    @SequenceGenerator(name = "action_logs_seq", sequenceName = "action_logs_seq", allocationSize = 50)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName="id")
    private Employee employee;
    @Column(nullable = false)
    private LocalDateTime proceededAt;
    @Column(nullable = false)
    private String entityType;
    @Column(nullable = false)
    private long entityId;
    @Column(nullable = false)
    private String action;
}
