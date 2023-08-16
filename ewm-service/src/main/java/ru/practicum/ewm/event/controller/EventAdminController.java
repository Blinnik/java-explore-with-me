package ru.practicum.ewm.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.service.EventAdminService;
import ru.practicum.ewm.event.util.EventUtil;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventAdminController {
    EventAdminService eventAdminService;

    @GetMapping
    public List<EventFullDto> getAll(@RequestParam(required = false) List<Long> users,
                                     @RequestParam(required = false) List<EventState> states,
                                     @RequestParam(required = false) List<Long> categories,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                     PaginationConfig paginationConfig) {

        return eventAdminService.getAll(users, states, categories,
                rangeStart, rangeEnd, paginationConfig);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        EventUtil.checkEventDate(updateEventUserRequest.getEventDate(), 1);

        return eventAdminService.update(eventId, updateEventUserRequest);
    }
}