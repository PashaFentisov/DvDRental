package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategoryRestController.class);

    @GetMapping
    public ResponseEntity<PageResponse<CategoryDto>> getCategories(@RequestParam(required = false, defaultValue = "0") int page,
                                                                   @RequestParam(required = false, defaultValue = "10") int size,
                                                                   @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 categories at one time");
        }
        PageResponse<CategoryDto> allCategories = categoryService.getAllCategories(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allCategories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody @Valid CategoryDto categoryDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCategory);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).DELETE_ACCESS)")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
