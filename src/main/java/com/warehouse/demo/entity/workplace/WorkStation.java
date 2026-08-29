package com.warehouse.demo.entity.workplace;

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
@Table(name = "work_stations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkStation implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "work_station_seq")
    @SequenceGenerator(name = "work_station_seq", sequenceName = "work_station_seq", allocationSize = 1)
    private long id;
    @Column(unique = true, nullable = false)
    private String stationNumber;
    @Column(nullable = false)
    private String controlNumber;
    private String type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", referencedColumnName = "id")
    private Workshop workshop;
}
