package ru.practicum.ewm.event.service.mapper;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.service.mapper.CategoryMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.dto.EventFullDto;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.event.model.dto.NewEventDto;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.model.dto.UserShortDto;
import ru.practicum.ewm.user.service.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category) {
        Location location = newEventDto.getLocation();

        return Event.builder()
                .eventDate(newEventDto.getEventDate())
                .description(newEventDto.getDescription())
                .initiator(initiator)
                .locationLat(location.getLat())
                .locationLon(location.getLon())
                .paid(newEventDto.isPaid())
                .category(category)
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .createdOn(LocalDateTime.now())
                .requestModeration(newEventDto.isRequestModeration())
                .participantLimit(newEventDto.getParticipantLimit())
                .state(EventState.PENDING)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        UserShortDto userShortDto = UserMapper.toUserShortDto(event.getInitiator());
        Location location = new Location(event.getLocationLat(), event.getLocationLon());
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(event.getCategory());

        return EventFullDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .description(event.getDescription())
                .initiator(userShortDto)
                .location(location)
                .paid(event.isPaid())
                .category(categoryDto)
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .createdOn(event.getCreatedOn())
                .requestModeration(event.isRequestModeration())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(event.getConfirmedRequests())
                .publishedOn(event.getPublishedOn())
                .views(event.getViews())
                .state(event.getState())
                .build();
    }

    public static List<EventFullDto> toEventFullDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto toEventShortDto(Event event) {
        UserShortDto userShortDto = UserMapper.toUserShortDto(event.getInitiator());
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(event.getCategory());

        return EventShortDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .initiator(userShortDto)
                .paid(event.isPaid())
                .category(categoryDto)
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getViews())
                .build();
    }

    public static List<EventShortDto> toEventShortDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }
}
