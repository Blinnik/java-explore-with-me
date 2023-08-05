package ru.practicum.server.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.dto.EndpointHit;
import ru.practicum.common.dto.ViewStats;
import ru.practicum.server.model.dto.GetStatsParams;
import ru.practicum.server.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatsController {
    StatsService statsService;

    @PostMapping("/hit")
    public EndpointHit saveHit(@RequestBody @Valid EndpointHit hit) {
        return statsService.saveHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getStats(@Valid GetStatsParams params) {
        return statsService.getStats(params);
    }
}
