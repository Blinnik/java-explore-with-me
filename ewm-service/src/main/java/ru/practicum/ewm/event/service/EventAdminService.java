package ru.practicum.ewm.event.service;

import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {

    List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categoriesIds,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, PaginationConfig paginationConfig);

    EventFullDto updateEvent(Long eventId, UpdateEventUserRequest updateEventUserRequest);
}
