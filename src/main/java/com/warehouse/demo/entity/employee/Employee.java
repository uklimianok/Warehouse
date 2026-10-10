package com.warehouse.demo.entity.employee;

import java.time.LocalDate;

import com.warehouse.demo.entity.Identifiable;
import com.warehouse.demo.entity.workplace.Gate;
import com.warehouse.demo.entity.workplace.Workshop;

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
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Employee implements Identifiable {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "employee_seq"
    )
    @SequenceGenerator(
        name = "employee_seq", 
        sequenceName = "employee_seq", 
        allocationSize = 50
    )
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "employer_organization_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private Organization employerOrganization;
    @Column(unique = true, nullable = false)
    private String employeeNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "position_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private Position position;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "shift_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private Shift shift;
    @Column(nullable = true)
    private LocalDate birthDate;
    @Column(nullable = true)
    private String documentId;
    @Column(nullable = true)
    private String residenceAddress;
    @Column(nullable = true)
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "workshop_id", 
        referencedColumnName = "id", 
        nullable = true
    )
    private Workshop workshop;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "gate_id", 
        referencedColumnName = "id", 
        nullable = true
    )
    private Gate gate;
}
