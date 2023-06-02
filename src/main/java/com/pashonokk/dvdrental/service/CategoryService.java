package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.entity.Category;
import com.pashonokk.dvdrental.mapper.CategoryMapper;
import com.pashonokk.dvdrental.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void addCategory(CategoryDto categoryDto) {
        categoryRepository.save(categoryMapper.toEntity(categoryDto));
    }

    @Transactional
    public void partialUpdateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).orElse(null);
        if (category == null) {
            return;
        }
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        if (categoryDto.getLastUpdate() != null) {
            category.setLastUpdate(categoryDto.getLastUpdate());
        }
    }

}
