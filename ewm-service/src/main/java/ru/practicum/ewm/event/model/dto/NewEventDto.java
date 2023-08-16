package ru.practicum.ewm.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.event.model.Location;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewEventDto {
    @NotBlank(message = "annotation cannot be blank")
    @Length(message = "annotation: the string length should be from 20 to 2000 characters", min = 20, max = 2000)
    String annotation;

    Long category;

    @NotBlank(message = "description cannot be blank")
    @Length(message = "description: the string length should be from 20 to 7000 characters", min = 20, max = 7000)
    String description;

    @NotNull(message = "eventDate cannot be null")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future(message = "eventDate must be in the future")
    LocalDateTime eventDate;

    @NotNull(message = "location cannot be null")
    Location location;

    boolean paid;

    int participantLimit;

    boolean requestModeration = true;

    @NotNull(message = "title cannot be null")
    @Length(message = "title: the string length should be from 3 to 120 characters", min = 3, max = 120)
    String title;
}
