package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.LanguageDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Language;
import com.pashonokk.dvdrental.mapper.LanguageMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.LanguageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;
    private final PageMapper pageMapper;
    private static final String ERROR_MESSAGE = "Language with id %s doesn't exist";

    @Transactional(readOnly = true)
    public PageResponse<LanguageDto> getAllLanguages(Pageable pageable) {
        return pageMapper.toPageResponse(languageRepository.findAll(pageable).map(languageMapper::toDto));
    }

    @Transactional(readOnly = true)
    public LanguageDto getLanguageById(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        return languageMapper.toDto(language);
    }

    @Transactional
    public LanguageDto addLanguage(LanguageDto languageDto) {
        Language language = languageMapper.toEntity(languageDto);
        Language savedLanguage = languageRepository.save(language);
        return languageMapper.toDto(savedLanguage);

    }

    @Transactional
    public void deleteLanguageById(Long id) {
        Language language = languageRepository.findByIdWithFilms(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        language.removeFilms(new HashSet<>(language.getFilms()));
        languageRepository.delete(language);
    }

    @Transactional
    public LanguageDto updateSomeFieldsOfLanguage(LanguageDto languageDto) {
        Language language = languageRepository.findById(languageDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, languageDto.getId())));

        Optional.ofNullable(languageDto.getName()).ifPresent(language::setName);
        Optional.ofNullable(languageDto.getLastUpdate()).ifPresent(language::setLastUpdate);
        return languageMapper.toDto(language);
    }
}
