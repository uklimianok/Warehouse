package com.warehouse.demo.service.item.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.item.paperCard.PaperCardRequest;
import com.warehouse.demo.entity.item.PaperCard;
import com.warehouse.demo.mapper.item.paperCard.PaperCardRequestMapper;
import com.warehouse.demo.repository.item.PaperCardRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.item.PaperCardService;
import com.warehouse.demo.util.EntityName;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaperCardServiceImpl extends AbstractService<PaperCard, Long> implements PaperCardService {
    private final PaperCardRepository paperCardRepository;

    private final PaperCardRequestMapper paperCardRequestMapper;

    @Override 
    @Cacheable(value = "paperCards", key = "#id")
    public PaperCard read(Long id) {
        return super.read(id);
    }

    @Override
    public PaperCard create(PaperCardRequest paperCardRequest) {
        return modifyAndSave(new PaperCard(), paperCardRequest);
    }

    @Override
    @CacheEvict(value = "paperCards", key = "#id")
    public PaperCard update(long id, PaperCardRequest paperCardRequest) {
        return modifyAndSave(read(id), paperCardRequest);
    }

    @Override 
    @CacheEvict(value = "paperCards", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private PaperCard modifyAndSave(PaperCard target, PaperCardRequest from) {
        paperCardRequestMapper.convertFromRequest(from, target);
        return paperCardRepository.save(target);
    }

    @Override
    protected JpaRepository<PaperCard, Long> getRepository() {
        return paperCardRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.PAPER_CARD;
    }
}
