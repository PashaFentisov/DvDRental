package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.entity.Category;
import com.pashonokk.dvdrental.entity.Film;
import com.pashonokk.dvdrental.mapper.CategoryMapper;
import com.pashonokk.dvdrental.mapper.impl.PageMapperImpl;
import com.pashonokk.dvdrental.pageFeature.GeneralPageResponse;
import com.pashonokk.dvdrental.repository.CategoryRepository;
import com.pashonokk.dvdrental.repository.FilmRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final FilmRepository filmRepository;
    private final CategoryMapper categoryMapper;
    private final PageMapperImpl pageMapperImpl;
    private static final String ERROR_MESSAGE = "Category with id %s doesn't exist";

    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
    }

    @Transactional(readOnly = true)
    public GeneralPageResponse<CategoryDto> getAllCategories(Pageable pageable) {
        return pageMapperImpl.toGeneralResponse(categoryRepository.findAll(pageable).map(categoryMapper::toDto));
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        Category category = categoryRepository.findByIdWithFilms(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        List<Film> films = filmRepository.findByLanguageIdWithLanguages(id);
        for (Film film : films) {
            film.removeCategory(category);
        }
        categoryRepository.deleteById(id);
    }

    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category savedCategory = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        return categoryMapper.toDto(savedCategory);
    }

    @Transactional
    public CategoryDto partialUpdateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, categoryDto.getId())));
        Optional.ofNullable(categoryDto.getName()).ifPresent(category::setName);
        Optional.ofNullable(categoryDto.getLastUpdate()).ifPresent(category::setLastUpdate);
        return categoryMapper.toDto(category);
    }

}
