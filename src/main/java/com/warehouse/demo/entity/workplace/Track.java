package com.warehouse.demo.entity.workplace;

import java.math.BigDecimal;

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
@Table(name = "tracks")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Track implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "track_seq")
    @SequenceGenerator(name = "track_seq", sequenceName = "track_seq", allocationSize = 1)
    private long id;
    @Column(unique = true, nullable = false)
    private String symbol;
    @Column(nullable = false)
    private BigDecimal length;
    @Column(nullable = false)
    private BigDecimal width;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_id", referencedColumnName = "id")
    private Gate gate;
}
