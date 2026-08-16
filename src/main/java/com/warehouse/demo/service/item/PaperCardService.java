package com.warehouse.demo.service.item;

import com.warehouse.demo.dto.item.paperCard.PaperCardRequest;
import com.warehouse.demo.entity.item.PaperCard;
import com.warehouse.demo.service.BaseService;

public interface PaperCardService extends BaseService<PaperCard, Long> {
    PaperCard create(PaperCardRequest paperCardRequest);
    PaperCard update(long id, PaperCardRequest paperCardRequest);
}
