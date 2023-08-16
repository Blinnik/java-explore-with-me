package ru.practicum.ewm.event.util;

import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.event.model.dto.UpdateEventState;
import ru.practicum.ewm.event.model.dto.UpdateEventUserRequest;
import ru.practicum.exceptionslibrary.exception.BadRequestException;

import java.time.LocalDateTime;

public class EventUtil {
    public static void updateFields(UpdateEventUserRequest updateEventUserRequest, Event event) {
        String annotation = updateEventUserRequest.getAnnotation();
        Long category = updateEventUserRequest.getCategory();
        String description = updateEventUserRequest.getDescription();
        LocalDateTime eventDate = updateEventUserRequest.getEventDate();
        Location location = updateEventUserRequest.getLocation();
        Boolean paid = updateEventUserRequest.getPaid();
        Integer participantLimit = updateEventUserRequest.getParticipantLimit();
        Boolean requestModeration = updateEventUserRequest.getRequestModeration();
        String stateAction = updateEventUserRequest.getStateAction();
        String title = updateEventUserRequest.getTitle();

        if (annotation != null) {
            event.setAnnotation(annotation);
        }

        if (category != null) {
            event.setCategory(Category.builder().id(category).build());
        }

        if (description != null) {
            event.setDescription(description);
        }

        if (eventDate != null) {
            event.setEventDate(eventDate);
        }

        if (location != null) {
            Float locationLat = location.getLat();
            Float locationLon = location.getLon();

            if (locationLat != null) {
                event.setLocationLat(locationLat);
            }
            if (locationLon != null) {
                event.setLocationLon(locationLon);
            }
        }

        if (paid != null) {
            event.setPaid(paid);
        }

        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }

        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }

        if (stateAction != null) {
            UpdateEventState updateEventState = UpdateEventState.valueOf(stateAction);

            if (updateEventState.equals(UpdateEventState.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
            } else if (updateEventState.equals(UpdateEventState.REJECT_EVENT) ||
                    updateEventState.equals(UpdateEventState.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            } else if (updateEventState.equals(UpdateEventState.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            }
        }

        if (title != null) {
            event.setTitle(title);
        }
    }

    public static void checkEventDate(LocalDateTime dateTime, int hoursBeforeEvent) {
        if (dateTime == null) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(dateTime.minusHours(hoursBeforeEvent))) {
            throw new BadRequestException("EventTime should be a " + hoursBeforeEvent + " hours from the current time");
        }
    }
}
