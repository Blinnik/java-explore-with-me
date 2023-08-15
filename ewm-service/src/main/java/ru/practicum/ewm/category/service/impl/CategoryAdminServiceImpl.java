package ru.practicum.ewm.category.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.model.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.dto.CategoryDto;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.category.service.CategoryAdminService;
import ru.practicum.ewm.category.service.mapper.CategoryMapper;
import ru.practicum.exceptionslibrary.exception.NotFoundException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminServiceImpl implements CategoryAdminService {
    CategoryRepository repository;

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);

        Category createdCategory = repository.save(category);
        log.debug("New category was created, id = {}", createdCategory.getId());

        return CategoryMapper.toCategoryDto(createdCategory);
    }

    @Override
    public void delete(Long catId) {
        if (!repository.existsById(catId)) {
            throw new NotFoundException(String.format("Category with id=%s was not found", catId));
        }

        repository.deleteById(catId);
        log.debug("A category with an id {} was deleted", catId);
    }

    @Override
    public CategoryDto update(Long catId, CategoryDto categoryDto) {
        if (!repository.existsById(catId)) {
            throw new NotFoundException(String.format("Category with id=%s was not found", catId));
        }

        Category category = CategoryMapper.toCategory(catId, categoryDto);

        Category createdCategory = repository.save(category);
        log.debug("A category with an id {} was updated", category.getId());

        return CategoryMapper.toCategoryDto(createdCategory);
    }
}
