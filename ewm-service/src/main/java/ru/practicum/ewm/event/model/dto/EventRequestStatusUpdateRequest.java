package ru.practicum.ewm.event.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.request.model.RequestStatus;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;

    RequestStatus status;
}
