package com.warehouse.demo.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.item.Pallet;

public interface PalletRepository extends JpaRepository<Pallet, Long> {}
