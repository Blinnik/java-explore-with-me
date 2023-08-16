package ru.practicum.ewm.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.event.util.EventUtil;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventPrivateController {
    EventPrivateService eventPrivateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@PathVariable Long userId,
                               @RequestBody @Valid NewEventDto newEventDto) {
        EventUtil.checkEventDate(newEventDto.getEventDate(), 2);

        return eventPrivateService.create(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             PaginationConfig paginationConfig) {
        return eventPrivateService.getUserEvents(userId, paginationConfig);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getOne(@PathVariable Long userId,
                               @PathVariable Long eventId) {
        return eventPrivateService.getOne(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        EventUtil.checkEventDate(updateEventUserRequest.getEventDate(), 2);

        return eventPrivateService.update(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        return eventPrivateService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody EventRequestStatusUpdateRequest updateRequest) {
        return eventPrivateService.updateRequests(userId, eventId, updateRequest);
    }
}
