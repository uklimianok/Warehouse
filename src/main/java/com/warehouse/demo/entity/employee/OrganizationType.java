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
@Table(name = "organization_types")
@NoArgsConstructor
@AllArgsConstructor 
@Getter
@Setter
public class OrganizationType implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_type_seq")
    @SequenceGenerator(name = "organization_type_seq", sequenceName = "organization_type_seq", allocationSize = 1)
    private long id;
    @Column(unique = true)
    private String name;
}
