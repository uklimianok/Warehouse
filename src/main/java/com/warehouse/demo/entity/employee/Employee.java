package com.warehouse.demo.entity.employee;

import java.time.LocalDate;

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
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    @SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 50)
    private long id;
    private String firstName;
    private String lastName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", referencedColumnName = "id")
    private Organization organization;
    private int employeeNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private Position position;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    private Shift shift;
    private LocalDate birDate;
    private String documentId;
    private String residenceAddress;
    private String phoneNumber;
}
