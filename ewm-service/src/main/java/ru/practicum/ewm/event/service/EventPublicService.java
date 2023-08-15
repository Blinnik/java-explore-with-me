package ru.practicum.ewm.event.service;

import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {

    EventFullDto getEvent(Long eventId, HttpServletRequest request);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                  PaginationConfig paginationConfig, HttpServletRequest request);
}
