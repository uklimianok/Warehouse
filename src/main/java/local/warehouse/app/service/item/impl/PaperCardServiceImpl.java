package local.warehouse.app.service.item.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.item.paperCard.PaperCardRequest;
import local.warehouse.app.entity.item.PaperCard;
import local.warehouse.app.mapper.item.paperCard.PaperCardRequestMapper;
import local.warehouse.app.repository.item.PaperCardRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.item.PaperCardService;
import local.warehouse.app.util.info.Entity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaperCardServiceImpl extends AbstractService<PaperCard, Long> implements PaperCardService {
    private final PaperCardRepository paperCardRepository;

    private final PaperCardRequestMapper paperCardRequestMapper;

    @Override
    public PaperCard read(Long id) {
        return super.read(id);
    }

    @Override
    public PaperCard create(PaperCardRequest paperCardRequest) {
        return modifyAndSave(new PaperCard(), paperCardRequest);
    }

    @Override
    public PaperCard update(long id, PaperCardRequest paperCardRequest) {
        return modifyAndSave(read(id), paperCardRequest);
    }

    @Override
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
    protected Entity getEntityName() {
        return Entity.PAPER_CARD;
    }
}
