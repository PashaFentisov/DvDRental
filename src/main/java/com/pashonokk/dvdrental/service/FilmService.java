package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Actor;
import com.pashonokk.dvdrental.entity.Category;
import com.pashonokk.dvdrental.entity.Film;
import com.pashonokk.dvdrental.entity.Language;
import com.pashonokk.dvdrental.exception.FilmWithoutLanguageException;
import com.pashonokk.dvdrental.mapper.*;
import com.pashonokk.dvdrental.repository.ActorRepository;
import com.pashonokk.dvdrental.repository.CategoryRepository;
import com.pashonokk.dvdrental.repository.FilmRepository;
import com.pashonokk.dvdrental.repository.LanguageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmRepository filmRepository;
    private final PageMapper pageMapper;
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final ActorRepository actorRepository;
    private final FilmMapper filmMapper;
    private final StoreMapper storeMapper;
    private final CategoryMapper categoryMapper;
    private final ActorMapper actorMapper;
    private final FilmSavingMapper filmSavingMapper;
    private static final String FILM_ERROR_MESSAGE = "Film with id %s doesn't exist";
    private static final String CATEGORY_ERROR_MESSAGE = "Category with id %s doesn't exist";

    @Transactional(readOnly = true)
    public PageResponse<FilmDto> getAllFilms(Pageable pageable) {
        return pageMapper.toPageResponse(filmRepository.findAll(pageable).map(filmMapper::toDto));
    }

    @Transactional(readOnly = true)
    public FilmDto getFilmWithCategoriesAndLanguages(Long id) {
        Film film = filmRepository.findByIdWithCategoriesAndLanguages(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        return filmMapper.toDto(film);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getFilmsCategories(Long id) {
        Film film = filmRepository.findByIdWithCategories(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        return film.getCategories().stream().map(categoryMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<ActorDto> getFilmsActors(Long id) {
        Film film = filmRepository.findByIdWithActors(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        return film.getActors().stream().map(actorMapper::toDto).toList();
    }
    @Transactional(readOnly = true)
    public List<StoreDto> getFilmStores(Long id) {
        Film film = filmRepository.getFilmById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        return film.getInventories().stream().map(inventory -> storeMapper.toDto(inventory.getStore())).toList();
    }

    @Transactional
    public FilmDto addFilm(FilmSavingDto filmSavingDto) {
        if (filmSavingDto.getLanguagesIds() == null) {
            throw new FilmWithoutLanguageException("Provide a valid Language for the Film");
        }
        List<Category> categoriesById = Collections.emptyList();
        if (filmSavingDto.getCategoriesIds() != null) {
            categoriesById = categoryRepository.findAllByIdAndFilms(filmSavingDto.getCategoriesIds());
        }
        List<Actor> actorsByIds = Collections.emptyList();
        if (filmSavingDto.getActorsIds() != null) {
            actorsByIds = actorRepository.findAllByIdAndFilms(filmSavingDto.getActorsIds());
        }
        List<Language> languagesById = languageRepository.findAllByIdAndFilms(filmSavingDto.getLanguagesIds());
        Film film = filmSavingMapper.toEntity(filmSavingDto);
        film.addCategory(categoriesById);
        film.addLanguage(languagesById);
        film.addActor(actorsByIds);
        Film savedFilm = filmRepository.save(film);
        return filmMapper.toDto(savedFilm);
    }

    @Transactional
    public void deleteFilmById(Long id) {
        Film film = filmRepository.getFilmById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        film.removeInventories(film.getInventories());
        filmRepository.delete(film);
    }

    @Transactional
    public FilmDto updateSomeFieldsOfFilm(FilmDto filmDto) {
        Film film = filmRepository.findByIdWithCategories(filmDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, filmDto.getId())));

        Optional.ofNullable(filmDto.getTitle()).ifPresent(film::setTitle);
        Optional.ofNullable(filmDto.getDescription()).ifPresent(film::setDescription);
        Optional.ofNullable(filmDto.getReleaseYear()).ifPresent(film::setReleaseYear);
        Optional.ofNullable(filmDto.getRentalDuration()).ifPresent(film::setRentalDuration);
        Optional.ofNullable(filmDto.getRentalRate()).ifPresent(film::setRentalRate);
        Optional.ofNullable(filmDto.getLength()).ifPresent(film::setLength);
        Optional.ofNullable(filmDto.getReplacementCost()).ifPresent(film::setReplacementCost);
        Optional.ofNullable(filmDto.getRating()).ifPresent(film::setRating);
        Optional.ofNullable(filmDto.getLastUpdate()).ifPresent(film::setLastUpdate);
        return filmMapper.toDto(film);
    }

    @Transactional
    public FilmDto addCategoryToFilm(Long id, Long categoryId) {
        Film film = filmRepository.findByIdWithCategories(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORY_ERROR_MESSAGE, categoryId)));
        film.addCategory(category);
        return filmMapper.toDto(film);
    }
    @Transactional(readOnly = true)
    public List<FilmDto> getFilmsWithCategoriesAndLanguagesByLanguage(String language) {
        List<Film> filmsByLanguage = filmRepository.findByIdWithCategoriesAndLanguagesByLanguage(language);
        return filmsByLanguage.stream().map(filmMapper::toDto).toList();
    }
}
