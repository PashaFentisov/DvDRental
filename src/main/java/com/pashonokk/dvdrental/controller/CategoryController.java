package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.entity.Category;
import com.pashonokk.dvdrental.service.CategoryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public static final String REDIRECT_TO_ALL = "categories";

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping
    public Iterable<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PostMapping
    public RedirectView addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PutMapping("/{id}")
    public RedirectView updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        categoryService.addCategory(category);
        return new RedirectView(REDIRECT_TO_ALL);
    }

}
