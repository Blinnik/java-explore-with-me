package ru.practicum.server.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.common.dto.EndpointHit;
import ru.practicum.common.dto.ViewStats;
import ru.practicum.server.model.Stats;
import ru.practicum.server.model.dto.GetStatsParams;
import ru.practicum.server.repository.StatsRepository;
import ru.practicum.server.service.mapper.StatsMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    StatsRepository statsRepository;

    @Override
    public EndpointHit saveHit(EndpointHit hit) {
        Stats stats = StatsMapper.toStats(hit);

        statsRepository.save(stats);
        log.debug("hit was saved");

        return hit;
    }

    @Override
    public List<ViewStats> getStats(GetStatsParams params) {
        LocalDateTime start = params.getStart();
        LocalDateTime end = params.getEnd();
        List<String> uris = params.getUris();
        Boolean unique = params.getUnique();

        if (uris.size() == 0) {
            uris = statsRepository.findAllUris();
        }

        List<ViewStats> viewStatsList;
        if (unique) {
            viewStatsList = statsRepository.findAllByTimestampBetweenAndUriInAndIpUnique(start, end, uris);
            log.debug("statistics with unique ip addresses were obtained");
        } else {
            viewStatsList = statsRepository.findAllByTimestampBetweenAndUriIn(start, end, uris);
            log.debug("statistics without unique ip addresses were obtained");
        }

        return viewStatsList;
    }
}
