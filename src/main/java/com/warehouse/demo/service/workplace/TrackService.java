package com.warehouse.demo.service.workplace;

import com.warehouse.demo.dto.workplace.track.TrackRequest;
import com.warehouse.demo.entity.workplace.Track;
import com.warehouse.demo.service.BaseService;

public interface TrackService extends BaseService<Track, Long> {
    Track create(TrackRequest trackRequest);
    Track update(long id, TrackRequest trackRequest);
}
