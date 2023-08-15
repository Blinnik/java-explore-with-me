package ru.practicum.ewm.request.service.mapper;

import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public static List<ParticipationRequestDto> toParticipationRequestDto(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
