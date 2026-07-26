package com.warehouse.demo.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.demo.entity.item.PaperCard;

public interface PaperCardRepository extends JpaRepository<PaperCard, Long> {}
