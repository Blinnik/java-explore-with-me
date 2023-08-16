package ru.practicum.ewm.event.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.dto.*;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventPrivateService;
import ru.practicum.ewm.event.service.mapper.EventMapper;
import ru.practicum.ewm.event.util.EventUtil;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestStatus;
import ru.practicum.ewm.request.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.request.service.mapper.RequestMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.exceptionslibrary.exception.ConflictException;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class EventPrivateServiceImpl implements EventPrivateService {
    EventRepository eventRepository;
    UserRepository userRepository;
    CategoryRepository categoryRepository;
    RequestRepository requestRepository;

    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%s was not found", userId)));

        Long catId = newEventDto.getCategory();
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%s was not found", catId)));

        Event event = EventMapper.toEvent(newEventDto, initiator, category);

        Event createdEvent = eventRepository.save(event);
        log.debug("New event was created, id = {}", createdEvent.getId());

        return EventMapper.toEventFullDto(createdEvent);
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, PaginationConfig paginationConfig) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        List<Event> events = eventRepository.findAllByInitiatorId(userId, paginationConfig.getPageable());
        log.debug("Events of the user with id {} were received", userId);

        return EventMapper.toEventShortDto(events);
    }

    @Override
    public EventFullDto getOne(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        // returns event if the user is the owner
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%s of user with id=%s was not found", eventId, userId)));
        log.debug("An event with id {} was received", eventId);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        Event foundEvent = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%s of user with id=%s was not found", eventId, userId)));

        if (foundEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        EventUtil.updateFields(updateEventUserRequest, foundEvent);

        Event updatedEvent = eventRepository.save(foundEvent);
        log.debug("An event with an id {} was updated", updatedEvent.getId());

        return EventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new NotFoundException(
                    String.format("The user with id=%s is not the owner of the event with id=%s", userId, eventId)
            );
        }

        List<Request> requests = requestRepository.findAllByEventId(eventId);
        log.debug("Requests of the event with id {} were received", eventId);

        return RequestMapper.toParticipationRequestDto(requests);
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(Long userId,
                                                         Long eventId,
                                                         EventRequestStatusUpdateRequest updateRequest) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found", userId));
        }

        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("The user with id=%s is not the owner of the event with id=%s", userId, eventId)
                ));

        RequestStatus status = updateRequest.getStatus();
        List<Long> requestIds = updateRequest.getRequestIds();

        int confirmedRequests = event.getConfirmedRequests();
        int participationLimit = event.getParticipantLimit();
        int requestsNumber = requestIds.size();

        if (status.equals(RequestStatus.CONFIRMED) &&
                participationLimit != 0 &&
                confirmedRequests + requestsNumber > participationLimit) {
            throw new ConflictException("It is not possible to accept such a number of requests due to the limit");
        }

        List<Request> requests = requestRepository.findAllByIdIn(requestIds);

        if (status.equals(RequestStatus.REJECTED)) {
            if (requests.stream().anyMatch(e -> e.getStatus().equals(RequestStatus.CONFIRMED))) {
                throw new ConflictException("It is not possible to cancel an already accepted request");
            }
        }

        for (Request request : requests) {
            request.setStatus(status);
        }
        requestRepository.saveAll(requests);
        log.debug("Requests were updated");

        if (status.equals(RequestStatus.CONFIRMED)) {
            confirmedRequests += requestsNumber;

            event.setConfirmedRequests(confirmedRequests);
            eventRepository.save(event);
            log.debug("Event's confirmed requests were updated");
        }

        List<Request> eventRequests = requestRepository.findAllByEventId(eventId);
        log.debug("Requests of the event with id {} were received", eventId);

        List<ParticipationRequestDto> confirmedRequestsList = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequestsList = new ArrayList<>();

        for (Request request : eventRequests) {
            RequestStatus requestStatus = request.getStatus();
            ParticipationRequestDto participationRequestDto = RequestMapper.toParticipationRequestDto(request);

            if (requestStatus.equals(RequestStatus.CONFIRMED)) {
                confirmedRequestsList.add(participationRequestDto);
            } else if (requestStatus.equals(RequestStatus.REJECTED)) {
                rejectedRequestsList.add(participationRequestDto);
            }
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequestsList)
                .rejectedRequests(rejectedRequestsList)
                .build();
    }
}
