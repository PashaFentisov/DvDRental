package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryDto categoryDto);
    CategoryDto toDto(Category category);
}
