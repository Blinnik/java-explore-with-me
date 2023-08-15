package ru.practicum.server.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.common.dto.EndpointHit;
import ru.practicum.common.dto.ViewStats;
import ru.practicum.exceptionslibrary.exception.BadRequestException;
import ru.practicum.server.model.param.GetStatsParams;
import ru.practicum.server.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class StatsController {
    StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit save(@RequestBody @Valid EndpointHit hit) {
        return statsService.save(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getAll(@Valid GetStatsParams params) {
        LocalDateTime start = params.getStart();
        LocalDateTime end = params.getEnd();

        if (end.isBefore(start)) {
            throw new BadRequestException("The end should be later than the start");
        }

        return statsService.getAll(params);
    }
}
