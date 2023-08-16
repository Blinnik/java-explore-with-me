package ru.practicum.ewm.request.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.request.service.RequestPrivateService;
import ru.practicum.ewm.request.service.mapper.RequestMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.exceptionslibrary.exception.ConflictException;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RequestPrivateServiceImpl implements RequestPrivateService {
    RequestRepository requestRepository;
    EventRepository eventRepository;
    UserRepository userRepository;

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%s was not found", userId)));

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("The request cannot be sent again");
        }

        if (eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new ConflictException("The user cannot send a request to his event");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%s was not found", eventId)));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("The user cannot participate in an unpublished event");
        }

        int participationLimit = event.getParticipantLimit();
        if (participationLimit != 0 && event.getConfirmedRequests() == participationLimit) {
            throw new ConflictException("The number of requests for participation in the event has reached a maximum");
        }

        Request request = Request.builder()
                .requester(requester)
                .event(event)
                .created(LocalDateTime.now())
                .build();

        if (event.isRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);

            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        Request createdRequest = requestRepository.save(request);
        log.debug("New request was created, id = {}", createdRequest.getId());

        return RequestMapper.toParticipationRequestDto(createdRequest);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.debug("A list of user requests was received");

        return RequestMapper.toParticipationRequestDto(requests);
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%s was not found", requestId)));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ConflictException("The user cannot cancel someone else's request");
        }

        if (request.getStatus().equals(RequestStatus.CANCELED)) {
            throw new ConflictException("The user cannot cancel his request again");
        }

        request.setStatus(RequestStatus.CANCELED);
        Request updatedRequest = requestRepository.save(request);
        log.debug("A request with an id {} was canceled", updatedRequest.getId());

        return RequestMapper.toParticipationRequestDto(updatedRequest);
    }
}
