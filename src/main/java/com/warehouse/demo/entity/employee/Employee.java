package com.warehouse.demo.entity.employee;

import java.time.LocalDate;

import com.warehouse.demo.entity.Identifiable;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    @SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 50)
    private long id;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_organization_id", referencedColumnName = "id")
    private Organization employerOrganization;
    @Column(unique = true, nullable = false)
    private String employeeNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", referencedColumnName = "id")
    private Position position;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shift_id", referencedColumnName = "id")
    private Shift shift;
    private LocalDate birthDate;
    private String documentId;
    private String residenceAddress;
    private String phoneNumber;
}
