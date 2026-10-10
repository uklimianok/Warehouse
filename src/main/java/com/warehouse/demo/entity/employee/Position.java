package com.warehouse.demo.entity.employee;

import java.util.List;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.warehouse.demo.entity.Identifiable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "positions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Position implements Identifiable {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE, 
        generator = "position_seq"
    )
    @SequenceGenerator(
        name = "position_seq", 
        sequenceName = "position_seq", 
        allocationSize = 1
    )
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String codeName;
    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "department_id", 
        referencedColumnName = "id", 
        nullable = false
    )
    private Department department;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "position_inheritances", 
        joinColumns = @JoinColumn(name = "position_id"),
        inverseJoinColumns = @JoinColumn(name = "inherited_position_id")
    )
    private List<Position> inheritedPositions;
    @Column(name = "is_removable", nullable = false)
    private boolean removable;
    @Column(nullable = false, columnDefinition = "jsonb")   // columnDefinition clarifies this field to be "jsonb", not "json"
    @JdbcTypeCode(SqlTypes.JSON)    // Tells Hibernate to serialize it into JSON
    private Map<String, List<String>> controllerFlags;
}
