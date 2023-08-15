package ru.practicum.ewm.compilation.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.compilation.service.CompilationPublicService;
import ru.practicum.ewm.compilation.service.mapper.CompilationMapper;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicServiceImpl implements CompilationPublicService {
    CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, PaginationConfig paginationConfig) {
        List<Compilation> compilations;
        Pageable pageable = paginationConfig.getPageable();
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable).getContent();
        } else {
            compilations = compilationRepository.findAllByPinnedEquals(pinned, pageable);
        }

        log.debug("Compilations were received");

        return CompilationMapper.toCompilationDto(compilations);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Compilation with id=%s was not found", compId))
                );

        log.debug("A compilation with id {} was received", compId);

        return CompilationMapper.toCompilationDto(compilation);
    }
}
