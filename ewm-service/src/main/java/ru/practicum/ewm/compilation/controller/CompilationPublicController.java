package ru.practicum.ewm.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.ewm.compilation.model.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationPublicService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationPublicController {
    CompilationPublicService compilationPublicService;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       PaginationConfig paginationConfig) {
        return compilationPublicService.getAll(pinned, paginationConfig);
    }

    @GetMapping("/{compId}")
    public CompilationDto getOne(@PathVariable Long compId) {
        return compilationPublicService.getOne(compId);
    }
}
