package com.warehouse.demo.mapper.item.pallet;

import org.springframework.stereotype.Component;

import com.warehouse.demo.entity.item.Pallet;
import com.warehouse.demo.repository.item.PalletRepository;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component 
@RequiredArgsConstructor 
public class PalletResolver {
    private final PalletRepository palletRepository;

    public Pallet mapPallet(long palletId) {
        return palletRepository.findById(palletId)
            .orElseThrow(() -> new EntityNotFoundException(
                Utility.getOutputMessage(EntityName.PALLET, OutputMessage.NOT_FOUND)));
    }
}
