package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryService categoryService;


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

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedCategory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        CategoryDto updatedCategory = categoryService.partialUpdateCategory(categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }
}
