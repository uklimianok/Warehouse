package local.warehouse.app.service.item;

import local.warehouse.app.dto.item.paperCard.PaperCardRequest;
import local.warehouse.app.entity.item.PaperCard;
import local.warehouse.app.service.BaseService;

public interface PaperCardService extends BaseService<PaperCard, Long> {
    PaperCard create(PaperCardRequest paperCardRequest);
    PaperCard update(long id, PaperCardRequest paperCardRequest);
}
