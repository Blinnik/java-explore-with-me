package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.model.dto.NewCategoryDto;

public interface CategoryAdminService {
    CategoryDto create(NewCategoryDto categoryDto);

    void delete(Long catId);

    CategoryDto update(Long catId, CategoryDto categoryDto);
}
