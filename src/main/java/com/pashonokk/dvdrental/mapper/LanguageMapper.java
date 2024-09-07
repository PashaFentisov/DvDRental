package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.LanguageDto;
import com.pashonokk.dvdrental.entity.Language;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LanguageMapper {
    Language toEntity(LanguageDto languageDto);
    LanguageDto toDto(Language language);
}
