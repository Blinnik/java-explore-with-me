package ru.practicum.server.service.mapper;

import ru.practicum.common.dto.EndpointHit;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {
    public static Stats toStats(EndpointHit hit) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime;
        if (hit.getTimestamp() != null) {
            dateTime = LocalDateTime.parse(hit.getTimestamp(), formatter);
        } else {
            dateTime = LocalDateTime.now();
            hit.setTimestamp(dateTime.format(formatter));
        }

        return Stats.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(dateTime)
                .build();
    }

    public static EndpointHit toEndpointHit(Stats stats) {
        return EndpointHit.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .ip(stats.getIp())
                .timestamp(String.valueOf(stats.getTimestamp()))
                .build();
    }
}
