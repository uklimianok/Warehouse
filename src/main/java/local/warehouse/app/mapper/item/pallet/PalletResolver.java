package local.warehouse.app.mapper.item.pallet;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import local.warehouse.app.entity.item.Pallet;
import local.warehouse.app.repository.item.PalletRepository;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class PalletResolver {
    private final PalletRepository palletRepository;

    public Pallet mapPallet(long palletId) {
        return palletRepository.findById(palletId)
            .orElseThrow(() -> new EntityNotFoundException(
                MessageHandler.getOutputMessage(Entity.PALLET, OutputMessage.NOT_FOUND)));
    }
}
