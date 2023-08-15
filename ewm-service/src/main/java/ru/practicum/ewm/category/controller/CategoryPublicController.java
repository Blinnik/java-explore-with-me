package ru.practicum.ewm.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryPublicService;
import ru.practicum.ewm.common.param.PaginationConfig;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryPublicController {
    CategoryPublicService categoryPublicService;

    @GetMapping("/{catId}")
    public CategoryDto getOne(@PathVariable Long catId) {
        return categoryPublicService.getOne(catId);
    }

    @GetMapping
    public List<CategoryDto> getAll(PaginationConfig paginationConfig) {
        return categoryPublicService.getAll(paginationConfig);
    }
}

