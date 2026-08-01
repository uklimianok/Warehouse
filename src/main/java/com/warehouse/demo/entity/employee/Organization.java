package com.warehouse.demo.entity.employee;

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
@Table(name = "organizations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_seq")
    @SequenceGenerator(name = "organization_seq", sequenceName = "organization_seq", allocationSize = 50)
    private long id;
    private String name;
    @Column(unique = true)
    private String organizationNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_type_id", referencedColumnName = "id")
    private OrganizationType organizationType;
    private String address;
    private String phone;
    private String email;
    private String url;
}
