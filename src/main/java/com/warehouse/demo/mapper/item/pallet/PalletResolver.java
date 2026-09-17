package com.warehouse.demo.mapper.item.pallet;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.repository.item.PalletRepository;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class PalletResolver {
    private final PalletRepository palletRepository;

    public Pallet mapPallet(long palletId) {
        return palletRepository.findById(palletId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(Entity.PALLET, OutputMessage.NOT_FOUND)));
    }
}
