package ru.practicum.ewm.event.service.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.UpdateEventState;
import ru.practicum.ewm.event.model.dto.UpdateEventUserRequest;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventAdminService;
import ru.practicum.ewm.event.service.mapper.EventMapper;
import ru.practicum.ewm.event.util.EventUtil;
import ru.practicum.exceptionslibrary.exception.ConflictException;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class EventAdminServiceImpl implements EventAdminService {
    EventRepository eventRepository;
    QEvent qEvent = QEvent.event;

    @Override
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        PaginationConfig paginationConfig) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (users != null) {
            booleanBuilder.and(qEvent.initiator.id.in(users));
        }

        if (states != null) {
            booleanBuilder.and(qEvent.state.in(states));
        }

        if (categories != null) {
            booleanBuilder.and(qEvent.category.id.in(categories));
        }

        if (rangeStart != null && rangeEnd != null) {
            booleanBuilder.and(qEvent.eventDate.between(rangeStart, rangeEnd));
        }

        Predicate predicate = booleanBuilder.getValue();

        Pageable pageable = paginationConfig.getPageable();
        List<Event> events;
        if (predicate != null) {
            events = eventRepository.findAll(predicate, pageable).getContent();
        } else {
            events = eventRepository.findAll(pageable).getContent();
        }
        log.debug("Filtered events were received");

        return EventMapper.toEventFullDto(events);
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event foundEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%s was not found", eventId)));

        EventState oldEventState = foundEvent.getState();
        String stateAction = updateEventUserRequest.getStateAction();
        if (stateAction != null) {
            UpdateEventState newEventState = UpdateEventState.valueOf(stateAction.toUpperCase());

            if (!oldEventState.equals(EventState.PENDING) &&
                    newEventState.equals(UpdateEventState.PUBLISH_EVENT)) {
                throw new ConflictException("Only pending events can be published");
            }

            if (oldEventState.equals(EventState.PUBLISHED) &&
                    newEventState.equals(UpdateEventState.REJECT_EVENT)) {
                throw new ConflictException("Published events cannot be canceled");
            }

            if (newEventState.equals(UpdateEventState.PUBLISH_EVENT)) {
                foundEvent.setPublishedOn(LocalDateTime.now());
            }
        }

        EventUtil.updateEventFields(updateEventUserRequest, foundEvent);

        Event updatedEvent = eventRepository.save(foundEvent);
        log.debug("An event with an id {} was updated", updatedEvent.getId());

        return EventMapper.toEventFullDto(updatedEvent);
    }
}
