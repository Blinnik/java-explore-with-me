package ru.practicum.server.service;

import ru.practicum.common.dto.EndpointHit;
import ru.practicum.common.dto.ViewStats;
import ru.practicum.server.model.param.GetStatsParams;

import java.util.List;

public interface StatsService {
    EndpointHit save(EndpointHit hit);

    List<ViewStats> getAll(GetStatsParams params);
}
