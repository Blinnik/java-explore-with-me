package ru.practicum.ewm.compilation.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.event.model.dto.EventShortDto;

import java.util.List;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    Long id;

    List<EventShortDto> events;

    String title;

    boolean pinned;
}
