package ru.practicum.ewm.event.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.stats.StatsClient;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.common.util.TimeFormatter;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.QEvent;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventPublicService;
import ru.practicum.ewm.event.service.mapper.EventMapper;
import ru.practicum.ewm.event.util.StatsParser;
import ru.practicum.exceptionslibrary.exception.BadRequestException;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class EventPublicServiceImpl implements EventPublicService {
    final EventRepository eventRepository;
    final String appName = "ewm-service";
    final QEvent qEvent = QEvent.event;
    StatsClient statsClient;


    @Autowired
    public EventPublicServiceImpl(EventRepository eventRepository, @Value("${stats-server.url}") String serverUrl) {
        this.eventRepository = eventRepository;

        try {
            statsClient = new StatsClient(serverUrl);
            log.info("Stats service was successfully connected");
        } catch (RuntimeException e) {
            log.warn("An error occurred in the stats service");
            statsClient = null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getOne(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findByIdAndStateEquals(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%s was not found", eventId)));
        log.debug("An event with id {} was received", eventId);

        if (statsClient != null) {
            statsClient.save(request, appName);
            log.debug("The stats client saved the request to the statistics");

            long uniqueViews = getEventViews(statsClient, eventId, event.getCreatedOn(), true);
            long eventViews = event.getViews();

            if (uniqueViews != eventViews) {
                event.setViews(uniqueViews);
                eventRepository.save(event);
                log.debug("A views field in event with id {} was updated", eventId);
            }
        }

        return EventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd, Boolean onlyAvailable, String sort,
                                      PaginationConfig paginationConfig, HttpServletRequest request) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (text != null) {
            booleanBuilder.and(qEvent.annotation.containsIgnoreCase(text)
                    .or(qEvent.title.containsIgnoreCase(text)));
        }

        if (categories != null) {
            booleanBuilder.and(qEvent.category.id.in(categories));
        }

        if (paid != null) {
            booleanBuilder.and(qEvent.paid.eq(paid));
        }

        if (rangeStart != null && rangeEnd != null) {
            booleanBuilder.and(qEvent.eventDate.between(rangeStart, rangeEnd));
        }

        if (onlyAvailable) {
            BooleanExpression participantLimitEqTo0 = qEvent.participantLimit.eq(0);
            BooleanExpression confirmedRequestsSmallerThanLimit = qEvent.confirmedRequests.lt(qEvent.participantLimit);

            booleanBuilder.and(participantLimitEqTo0
                    .or(confirmedRequestsSmallerThanLimit));
        }

        Pageable pageable;
        if (sort != null) {
            Sort sortType;

            if (sort.equalsIgnoreCase("EVENT_DATE")) {
                sortType = Sort.by("eventDate");
            } else if (sort.equalsIgnoreCase("VIEWS")) {
                sortType = Sort.by("views");
            } else {
                throw new BadRequestException("The sorting value must be either EVENT_DATE or VIEWS");
            }

            pageable = paginationConfig.getPageable(sortType);
        } else {
            pageable = paginationConfig.getPageable();
        }

        Predicate predicate = booleanBuilder.getValue();
        List<Event> events;
        if (predicate != null) {
            events = eventRepository.findAll(predicate, pageable).getContent();
        } else {
            events = eventRepository.findAll(pageable).getContent();
        }
        log.debug("Filtered events were received");

        if (statsClient != null) {
            statsClient.save(request, appName);
            log.debug("The stats client saved the request to the statistics");
        }

        return EventMapper.toEventShortDto(events);
    }

    private static long getEventViews(StatsClient statsClient, Long eventId, LocalDateTime createdOn, boolean unique) {
        try {
            String dateCreated = TimeFormatter.formatLocalDateTime(createdOn);
            // taking into account milliseconds
            String now = TimeFormatter.formatLocalDateTime(LocalDateTime.now().plusSeconds(1));
            String uri = "/events/" + eventId;

            ResponseEntity<Object> getStatsResponse = statsClient.getAll(dateCreated, now, List.of(uri), unique);

            List<Object> objectList = StatsParser.parseGetStatsToListOfObjects(getStatsResponse);

            if (objectList.isEmpty()) {
                return 0L;
            }

            Map<String, Object> fieldsOfObject = StatsParser.parseObjectToFields(objectList.get(0));

            return StatsParser.parseViews(fieldsOfObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("An error occurred while parsing the response from stats-server");
        }
    }
}