package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Category;
import com.pashonokk.dvdrental.mapper.CategoryMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final PageMapper pageMapper;
    private static final String ERROR_MESSAGE = "Category with id %s doesn't exist";

    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
    }

    @Transactional(readOnly = true)
    public PageResponse<CategoryDto> getAllCategories(Pageable pageable) {
        return pageMapper.toPageResponse(categoryRepository.findAll(pageable).map(categoryMapper::toDto));
    }

    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category.setIsDeleted(false);
        category.setLastUpdate(OffsetDateTime.now());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findByIdWithFilms(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        category.setIsDeleted(true);
    }

}
