package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.entity.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pashonokk.dvdrental.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

}
