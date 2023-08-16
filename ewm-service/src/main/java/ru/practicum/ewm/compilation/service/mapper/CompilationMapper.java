package ru.practicum.ewm.compilation.service.mapper;

import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.NewCompilationDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.dto.EventShortDto;
import ru.practicum.ewm.event.service.mapper.EventMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();

        List<Long> eventIds = newCompilationDto.getEvents();
        if (eventIds != null) {
            for (Long eventId : eventIds) {
                events.add(Event.builder().id(eventId).build());
            }
        }

        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        List<Event> events = compilation.getEvents();
        List<EventShortDto> eventShortDtoList = EventMapper.toEventShortDto(events);

        return CompilationDto.builder()
                .id(compilation.getId())
                .events(eventShortDtoList)
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> toCompilationDto(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }
}
