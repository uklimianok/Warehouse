package com.warehouse.demo.service.workplace.impl;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.workplace.track.TrackRequest;
import com.warehouse.demo.entity.workplace.Track;
import com.warehouse.demo.repository.workplace.GateRepository;
import com.warehouse.demo.repository.workplace.TrackRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.workplace.TrackService;
import com.warehouse.demo.util.EntityName;
import com.warehouse.demo.util.OutputMessage;
import com.warehouse.demo.util.Utility;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl extends AbstractService<Track, Long> implements TrackService {
    private final TrackRepository trackRepository;
    private final GateRepository gateRepository;
    
    @Override
    public Track create(TrackRequest trackRequest) {
        if (trackRepository.existsBySymbol(trackRequest.getSymbol()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        Track track = new Track();

        return modifyAndSave(track, trackRequest);
    }

    @Override
    public Track update(long id, TrackRequest trackRequest) {
        Track track = read(id);
        boolean trackChanged = !track.getSymbol().equals(trackRequest.getSymbol());
        boolean trackExists = trackRepository.existsBySymbol(trackRequest.getSymbol());
        if (trackChanged && trackExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(track, trackRequest);
    }

    @Override
    protected JpaRepository<Track, Long> getRepository() {
        return trackRepository;
    }

    @Override
    protected EntityName getEntityName() {
        return EntityName.TRACK;
    }

    @Override
    protected boolean isUsed(Long id) {
        return false;
    }

    private Track modifyAndSave(Track target, TrackRequest from) {
        target.setSymbol(from.getSymbol());
        target.setLength(from.getLength());
        target.setWidth(from.getWidth());

        target.setGate(
            gateRepository.findById(from.getGateId())
                .orElseThrow(() ->
                    new EntityNotFoundException(Utility.getOutputMessage(EntityName.GATE, OutputMessage.NOT_FOUND))
            )
        );

        return trackRepository.save(target);
    }
}
