package ru.practicum.ewm.event.service;

import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface EventPrivateService {

    EventFullDto create(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getUserEvents(Long userId, PaginationConfig paginationConfig);

    EventFullDto getOne(Long userId, Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateRequests(Long userId,
                                                  Long eventId,
                                                  EventRequestStatusUpdateRequest updateRequest);
}
