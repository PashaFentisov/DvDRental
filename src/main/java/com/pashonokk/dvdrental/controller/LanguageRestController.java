package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.LanguageDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.LanguageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageRestController {
    private final LanguageService languageService;
    private final Logger logger = LoggerFactory.getLogger(LanguageRestController.class);


    @GetMapping
    public ResponseEntity<PageResponse<LanguageDto>> getLanguages(@RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "10") int size,
                                                                  @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 languages at one time");
        }
        PageResponse<LanguageDto> allLanguages = languageService.getAllLanguages(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allLanguages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDto> getLanguageById(@PathVariable Long id) {
        LanguageDto language = languageService.getLanguageById(id);
        return ResponseEntity.ok(language);
    }

    @PostMapping
    public ResponseEntity<LanguageDto> addLanguage(@RequestBody @Valid LanguageDto languageDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        LanguageDto savedLanguage = languageService.addLanguage(languageDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedLanguage.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedLanguage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLanguage(@PathVariable Long id) {
        languageService.deleteLanguageById(id);
        return ResponseEntity.noContent().build();
    }

}
