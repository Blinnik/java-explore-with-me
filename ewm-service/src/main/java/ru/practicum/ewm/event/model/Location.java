package ru.practicum.ewm.event.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@AllArgsConstructor
public class Location {
    @NotNull(message = "locationLat cannot be null")
    Float lat;

    @NotNull(message = "locationLon cannot be null")
    Float lon;
}
