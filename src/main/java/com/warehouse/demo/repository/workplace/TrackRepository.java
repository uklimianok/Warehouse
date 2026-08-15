package com.warehouse.demo.repository.workplace;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.workplace.Track;

public interface TrackRepository extends JpaRepository<Track, Long> {
    boolean existsByGateId(long id);
    boolean existsBySymbol(String symbol);
}
