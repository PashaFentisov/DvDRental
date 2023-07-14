package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.entity.Category;
import com.pashonokk.dvdrental.mapper.CategoryMapper;
import com.pashonokk.dvdrental.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

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
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category savedCategory = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    public CategoryDto partialUpdateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId()).orElse(null);
        if (category == null) {
            return null;
        }
        Optional.ofNullable(categoryDto.getName()).ifPresent(category::setName);
        Optional.ofNullable(categoryDto.getLastUpdate()).ifPresent(category::setLastUpdate);
        return categoryMapper.toDto(category);
    }

}
