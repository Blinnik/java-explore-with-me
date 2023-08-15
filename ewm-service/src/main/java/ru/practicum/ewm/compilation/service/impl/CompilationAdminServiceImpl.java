package ru.practicum.ewm.compilation.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.dto.UpdateCompilationRequest;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.CompilationAdminService;
import ru.practicum.ewm.compilation.service.mapper.CompilationMapper;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminServiceImpl implements CompilationAdminService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Long> eventIds = newCompilationDto.getEvents();
        List<Event> events = getEventsByIdList(eventIds);

        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(events);

        Compilation createdCompilation = compilationRepository.save(compilation);
        log.debug("New compilation was created, id = {}", createdCompilation.getId());

        return CompilationMapper.toCompilationDto(createdCompilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException(String.format("Compilation with id=%s was not found", compId));
        }

        compilationRepository.deleteById(compId);
        log.debug("A compilation with an id {} was deleted", compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation foundCompilation = compilationRepository.findById(compId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Compilation with id=%s was not found", compId))
                );

        List<Long> eventIds = updateCompilationRequest.getEvents();
        List<Event> events = getEventsByIdList(eventIds);

        updateCompilationFields(updateCompilationRequest, foundCompilation);

        Compilation updatedCompilation = compilationRepository.save(foundCompilation);
        log.debug("A compilation with an id {} was updated", updatedCompilation.getId());

        updatedCompilation.setEvents(events);

        return CompilationMapper.toCompilationDto(updatedCompilation);
    }

    private void updateCompilationFields(UpdateCompilationRequest updateCompilationRequest,
                                                Compilation compilation) {
        String title = updateCompilationRequest.getTitle();
        Boolean pinned = updateCompilationRequest.getPinned();
        List<Long> eventIds = updateCompilationRequest.getEvents();

        if (title != null) {
            compilation.setTitle(title);
        }

        if (pinned != null) {
            compilation.setPinned(pinned);
        }

        if (eventIds != null) {
            List<Event> events = new ArrayList<>();
            for (Long eventId : eventIds) {
                events.add(Event.builder().id(eventId).build());
            }

            compilation.setEvents(events);
        }
    }

    private List<Event> getEventsByIdList(List<Long> eventIds) {
        List<Event> events = new ArrayList<>();
        if (eventIds != null) {
            for (Long eventId : eventIds) {
                events.add(eventRepository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException(String.format("Event with id=%s was not found", eventId))));
            }
        }
        return events;
    }
}
