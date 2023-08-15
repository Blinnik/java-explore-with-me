package ru.practicum.ewm.event.service;

import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface EventPrivateService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getUserEvents(Long userId, PaginationConfig paginationConfig);

    EventFullDto getEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequests(Long userId,
                                                       Long eventId,
                                                       EventRequestStatusUpdateRequest updateRequest);
}
