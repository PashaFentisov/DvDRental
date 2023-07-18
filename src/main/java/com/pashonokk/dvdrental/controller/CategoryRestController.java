package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.dto.PageDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryRestController {
    private final CategoryService categoryService;


    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategory(id));
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> getCategories(@RequestBody PageDto pageDto) {
        if (pageDto.getSize() > 100) {
            throw new BigSizeException("You can get maximum 100 categories at one time");
        }
        Page<CategoryDto> allCategories = categoryService.getAllCategories(PageRequest.of(pageDto.getPage(), pageDto.getSize(), Sort.by(pageDto.getSort())));
        return ResponseEntity.ok(allCategories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        return ResponseEntity.created(URI.create("localhost:10000/categories/" + savedCategory.getId())).body(savedCategory);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateSomeFieldsOfCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        CategoryDto updatedCategory = categoryService.partialUpdateCategory(categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }
}
