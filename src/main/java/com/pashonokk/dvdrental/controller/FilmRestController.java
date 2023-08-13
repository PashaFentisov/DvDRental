package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.FilmService;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmRestController {
    private final FilmService filmService;
    private final Logger logger = LoggerFactory.getLogger(FilmRestController.class);


    @GetMapping
    public ResponseEntity<PageResponse<FilmDto>> getFilms(@RequestParam(required = false, defaultValue = "0") int page,
                                                          @RequestParam(required = false, defaultValue = "10") int size,
                                                          @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 films at one time");
        }
        PageResponse<FilmDto> allFilms = filmService.getAllFilms(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allFilms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Long id) {
        FilmDto filmDto = filmService.getFilmWithCategoriesAndLanguages(id);
        return ResponseEntity.ok(filmDto);
    }

    @GetMapping("/byLanguage/{language}")
    public ResponseEntity<List<FilmDto>> getFilmsByLanguage(@PathVariable String language) {
        List<FilmDto> filmsByLanguage = filmService.getFilmsWithCategoriesAndLanguagesByLanguage(language);
        return ResponseEntity.ok(filmsByLanguage);
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<CategoryDto>> getFilmsCategories(@PathVariable Long id) {
        List<CategoryDto> filmsCategories = filmService.getFilmsCategories(id);
        return ResponseEntity.ok(filmsCategories);
    }

    @GetMapping("/{id}/actors")
    public ResponseEntity<List<ActorDto>> getFilmsActors(@PathVariable Long id) {
        List<ActorDto> filmsActors = filmService.getFilmsActors(id);
        return ResponseEntity.ok(filmsActors);
    }

    @GetMapping("/{id}/stores")
    public ResponseEntity<List<StoreDto>> getFilmStores(@PathVariable Long id) {
        List<StoreDto> filmStores = filmService.getFilmStores(id);
        return ResponseEntity.ok(filmStores);
    }

    @PostMapping
    public ResponseEntity<FilmDto> addFilm(@RequestBody @Valid FilmSavingDto filmSavingDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        FilmDto savedFilm = filmService.addFilm(filmSavingDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedFilm.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedFilm);
    }

    @PostMapping("/{id}/categories/{categoryId}")
    public ResponseEntity<FilmDto> addCategoryToFilm(@PathVariable Long id, @PathVariable Long categoryId) {
        FilmDto updatedFilmDto = filmService.addCategoryToFilm(id, categoryId);
        return ResponseEntity.created(URI.create("localhost:10000/films/" + updatedFilmDto.getId())).body(updatedFilmDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilmById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FilmDto> updateFilm(@PathVariable Long id, @RequestBody FilmDto filmDto) {
        filmDto.setId(id);
        FilmDto updatedFilmDto = filmService.updateSomeFieldsOfFilm(filmDto);
        return ResponseEntity.ok(updatedFilmDto);
    }
}
