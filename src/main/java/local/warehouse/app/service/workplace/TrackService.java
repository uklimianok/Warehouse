package local.warehouse.app.service.workplace;

import local.warehouse.app.dto.workplace.track.TrackRequest;
import local.warehouse.app.entity.workplace.Track;
import local.warehouse.app.service.BaseService;

public interface TrackService extends BaseService<Track, Long> {
    Track create(TrackRequest trackRequest);
    Track update(long id, TrackRequest trackRequest);
}
