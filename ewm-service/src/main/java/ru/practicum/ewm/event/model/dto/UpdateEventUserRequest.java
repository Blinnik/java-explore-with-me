package ru.practicum.ewm.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.common.validation.constraint.NullOrNotBlank;
import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateEventUserRequest {

    @NullOrNotBlank(message = "annotation must not be blank")
    @Length(message = "annotation: the string length should be from 20 to 2000 characters", min = 20, max = 2000)
    String annotation;

    Long category;

    @NullOrNotBlank(message = "description must not be blank")
    @Length(message = "description: the string length should be from 20 to 7000 characters", min = 20, max = 7000)
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "eventDate must be in the future")
    LocalDateTime eventDate;

    Location location;

    Boolean paid;

    Integer participantLimit;

    Boolean requestModeration;

    @NullOrNotBlank(message = "stateAction must not be blank")
    @Length(message = "stateAction: the string length should be from 3 to 30 characters", min = 3, max = 30)
    String stateAction;

    @NullOrNotBlank(message = "title must not be blank")
    @Length(message = "title: the string length should be from 3 to 120 characters", min = 3, max = 120)
    String title;
}
