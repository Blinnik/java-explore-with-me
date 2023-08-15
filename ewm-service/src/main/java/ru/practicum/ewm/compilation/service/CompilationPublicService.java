package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getCompilations(Boolean pinned, PaginationConfig paginationConfig);

    CompilationDto getCompilation(Long compId);
}
