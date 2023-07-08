package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryService categoryService;

    public static final String REDIRECT_TO_ALL = "/categories";

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.getCategory(id);
    }

    @GetMapping
    public Page<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "10") int size,
                                           @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 elements");
        }
        return categoryService.getAllCategories(PageRequest.of(page, size, Sort.by(sort)));
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PostMapping
    public RedirectView addCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.addCategory(categoryDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PutMapping("/{id}")
    public RedirectView updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        categoryService.addCategory(categoryDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }

    @PatchMapping("/{id}")
    public RedirectView updateSomeFieldsOfCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        categoryService.partialUpdateCategory(categoryDto);
        return new RedirectView(REDIRECT_TO_ALL);
    }
}
