package ru.practicum.server.service;

import ru.practicum.common.dto.EndpointHit;
import ru.practicum.common.dto.ViewStats;
import ru.practicum.server.model.dto.GetStatsParams;

import java.util.List;

public interface StatsService {
    EndpointHit saveHit(EndpointHit hit);

    List<ViewStats> getStats(GetStatsParams params);
}
