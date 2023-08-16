package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService {
    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancel(Long userId, Long requestId);
}
