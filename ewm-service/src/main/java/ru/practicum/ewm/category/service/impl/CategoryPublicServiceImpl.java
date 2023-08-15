package ru.practicum.ewm.category.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryPublicService;
import ru.practicum.ewm.category.service.mapper.CategoryMapper;
import ru.practicum.ewm.common.param.PaginationConfig;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryPublicServiceImpl implements CategoryPublicService {
    CategoryRepository repository;


    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%s was not found", catId)));
        log.debug("A category with id {} was received", catId);

        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getCategories(PaginationConfig paginationConfig) {
        List<Category> categories = repository.findAll(paginationConfig.getPageable()).getContent();
        log.debug("A list of categories was received");

        return CategoryMapper.toCategoryDto(categories);
    }
}
