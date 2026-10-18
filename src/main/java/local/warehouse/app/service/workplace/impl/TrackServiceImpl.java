package local.warehouse.app.service.workplace.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import local.warehouse.app.dto.workplace.track.TrackRequest;
import local.warehouse.app.entity.workplace.Track;
import local.warehouse.app.mapper.workplace.track.TrackRequestMapper;
import local.warehouse.app.repository.workplace.TrackRepository;
import local.warehouse.app.service.AbstractService;
import local.warehouse.app.service.workplace.TrackService;
import local.warehouse.app.util.action.MessageHandler;
import local.warehouse.app.util.exception.BusinessRuleException;
import local.warehouse.app.util.info.Entity;
import local.warehouse.app.util.info.OutputMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrackServiceImpl extends AbstractService<Track, Long> implements TrackService {
    private final TrackRepository trackRepository;

    private final TrackRequestMapper trackRequestMapper;

    @Override
    public Track read(Long id) {
        return super.read(id);
    }
    
    @Override
    public Track create(TrackRequest trackRequest) {
        if (trackRepository.existsBySymbol(trackRequest.getSymbol()))
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(new Track(), trackRequest);
    }

    @Override
    public Track update(long id, TrackRequest trackRequest) {
        Track track = read(id);
        boolean trackChanged = !track.getSymbol().equals(trackRequest.getSymbol());
        boolean trackExists = trackRepository.existsBySymbol(trackRequest.getSymbol());
        if (trackChanged && trackExists)
            throw new BusinessRuleException(MessageHandler.getOutputMessage(getEntityName(), OutputMessage.EXISTS));

        return modifyAndSave(track, trackRequest);
    }

    @Override
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
