package com.warehouse.demo.service.workplace.impl;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.warehouse.demo.dto.workplace.track.TrackRequest;
import com.warehouse.demo.entity.workplace.Track;
import com.warehouse.demo.mapper.workplace.track.TrackRequestMapper;
import com.warehouse.demo.repository.workplace.TrackRepository;
import com.warehouse.demo.service.AbstractService;
import com.warehouse.demo.service.workplace.TrackService;
import com.warehouse.demo.util.action.Utility;
import com.warehouse.demo.util.info.Entity;
import com.warehouse.demo.util.info.OutputMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl extends AbstractService<Track, Long> implements TrackService {
    private final TrackRepository trackRepository;

    private final TrackRequestMapper trackRequestMapper;

    @Override 
    @Cacheable(value = "tracks", key = "#id")
    public Track read(Long id) {
        return super.read(id);
    }
    
    @Override
    public Track create(TrackRequest trackRequest) {
        if (trackRepository.existsBySymbol(trackRequest.getSymbol()))
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(new Track(), trackRequest);
    }

    @Override
    @CacheEvict(value = "tracks", key = "#id")
    public Track update(long id, TrackRequest trackRequest) {
        Track track = read(id);
        boolean trackChanged = !track.getSymbol().equals(trackRequest.getSymbol());
        boolean trackExists = trackRepository.existsBySymbol(trackRequest.getSymbol());
        if (trackChanged && trackExists)
            throw new DataIntegrityViolationException(Utility.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(track, trackRequest);
    }

    @Override 
    @CacheEvict(value = "tracks", key = "#id")
    public void delete(Long id) {
        super.delete(id);
    }

    private Track modifyAndSave(Track target, TrackRequest from) {
        trackRequestMapper.convertFromRequest(from, target);
        return trackRepository.save(target);
    }

    @Override
    protected JpaRepository<Track, Long> getRepository() {
        return trackRepository;
    }

    @Override
    protected Entity getEntityName() {
        return Entity.TRACK;
    }
}
