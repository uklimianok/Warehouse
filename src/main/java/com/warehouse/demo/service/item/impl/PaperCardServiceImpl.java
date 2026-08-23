package com.warehouse.demo.service.item.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.item.paperCard.PaperCardRequest;
import com.warehouse.demo.entity.item.PaperCard;
import com.warehouse.demo.repository.item.PaperCardRepository;
import com.warehouse.demo.repository.order.OrderPalletRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.item.PaperCardService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaperCardServiceImpl extends AbstractService<PaperCard, Long> implements PaperCardService {
    private final PaperCardRepository paperCardRepository;
    private final OrderPalletRepository orderPalletRepository;

    @Override
    public PaperCard create(PaperCardRequest paperCardRequest) {
        PaperCard paperCard = new PaperCard();

        return modifyAndSave(paperCard, paperCardRequest);
    }

    @Override
    public PaperCard update(long id, PaperCardRequest paperCardRequest) {
        PaperCard paperCard = read(id);

        return modifyAndSave(paperCard, paperCardRequest);
    }

    @Override
    protected JpaRepository<PaperCard, Long> getRepository() {
        return paperCardRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.PAPER_CARD;
    }

    private PaperCard modifyAndSave(PaperCard target, PaperCardRequest from) {
        target.setCode(from.getCode());

        target.setOrderPallet(
            orderPalletRepository.findById(from.getOrderPalletId())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.ORDER_PALLET, OutputMessage.NOT_FOUND))
            )
        );

        return paperCardRepository.save(target);
    }
}
