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
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class Department implements Identifiable {
    @Id 
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "department_seq"
    )
    @SequenceGenerator(
        name = "department_seq", 
        sequenceName = "department_seq", 
        allocationSize = 1
    )
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String codeName;
    @Column(nullable = false)
    private int priority;
    @Column(name = "is_removable", nullable = false)
    private boolean removable;
}
