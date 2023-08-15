package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.common.param.PaginationConfig;

import java.util.List;

public interface CategoryPublicService {

    CategoryDto getCategory(Long catId);

    List<CategoryDto> getCategories(PaginationConfig paginationConfig);
}
