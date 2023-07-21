package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.dto.FilmSavingDto;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmRestController {
    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<Page<FilmDto>> getFilms(@RequestParam(required = false, defaultValue = "0") int page,
                                                  @RequestParam(required = false, defaultValue = "10") int size,
                                                  @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 films at one time");
        }
        Page<FilmDto> allFilms = filmService.getAllFilms(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allFilms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Long id) {
        FilmDto filmDto = filmService.getFilmWithCategories(id);
        return ResponseEntity.ok(filmDto);
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<List<CategoryDto>> getFilmsCategories(@PathVariable Long id) {
        List<CategoryDto> filmsCategories = filmService.getFilmsCategories(id);
        return ResponseEntity.ok(filmsCategories);
    }

    @PostMapping
    public ResponseEntity<FilmDto> addFilm(@RequestBody FilmSavingDto filmSavingDto) {
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
    public ResponseEntity<FilmDto> updateSomeFieldsOfAddress(@PathVariable Long id, @RequestBody FilmDto filmDto) {
        filmDto.setId(id);
        FilmDto updatedFilmDto = filmService.updateSomeFieldsOfFilm(filmDto);
        return ResponseEntity.ok(updatedFilmDto);
    }
}
