package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.LanguageDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageRestController {
    private final LanguageService languageService;

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
    public ResponseEntity<LanguageDto> addLanguage(@RequestBody LanguageDto languageDto) {
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
