package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.CategoryDto;
import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.dto.FilmSavingDto;
import com.pashonokk.dvdrental.entity.Category;
import com.pashonokk.dvdrental.entity.Film;
import com.pashonokk.dvdrental.entity.Language;
import com.pashonokk.dvdrental.exception.FilmWithoutLanguageException;
import com.pashonokk.dvdrental.mapper.CategoryMapper;
import com.pashonokk.dvdrental.mapper.FilmMapper;
import com.pashonokk.dvdrental.mapper.FilmSavingMapper;
import com.pashonokk.dvdrental.repository.CategoryRepository;
import com.pashonokk.dvdrental.repository.FilmRepository;
import com.pashonokk.dvdrental.repository.LanguageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final CategoryRepository categoryRepository;
    private final LanguageRepository languageRepository;
    private final FilmMapper filmMapper;
    private final CategoryMapper categoryMapper;
    private final FilmSavingMapper filmSavingMapper;
    private static final String FILM_ERROR_MESSAGE = "Film with id %s doesn't exist";
    private static final String CATEGORY_ERROR_MESSAGE = "Category with id %s doesn't exist";
    private static final String LANGUAGE_ERROR_MESSAGE = "Language with id %s doesn't exist";

    @Transactional(readOnly = true)
    public Page<FilmDto> getAllFilms(Pageable pageable) {
        return filmRepository.findAll(pageable).map(filmMapper::toDto);
    }
    @Transactional(readOnly = true)
    public FilmDto getFilmWithCategories(Long id) {
        Film film = filmRepository.findByIdWithCategories(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        return filmMapper.toDto(film);
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getFilmsCategories(Long id) {
        Film film = filmRepository.findByIdWithCategories(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        return film.getCategories().stream().map(categoryMapper::toDto).toList();
    }

    @Transactional
    public FilmDto addFilm(FilmSavingDto filmSavingDto) {
        if(filmSavingDto.getLanguageId()==null){
            throw new FilmWithoutLanguageException("Provide a valid Language for the Film");
        }
        Language language = languageRepository.findById(filmSavingDto.getLanguageId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(LANGUAGE_ERROR_MESSAGE, filmSavingDto.getLanguageId())));
        List<Category> categoriesByIds = Collections.emptyList();
        if(filmSavingDto.getCategoryIds()!=null){
            categoriesByIds = categoryRepository.findAllById(filmSavingDto.getCategoryIds());
        }
        Film film = filmSavingMapper.toEntity(filmSavingDto);
        for (int i = 0; i < categoriesByIds.size(); i++) {
            film.getCategories().add(categoriesByIds.get(i));
        }
        language.addFilm(film);
        Film savedFilm = filmRepository.save(film);
        return filmMapper.toDto(savedFilm);

    }

    @Transactional
    public void deleteFilmById(Long id) {
        Film film = filmRepository.findByIdWithCategories(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        filmRepository.delete(film);
    }

    @Transactional
    public FilmDto updateSomeFieldsOfFilm(FilmDto filmDto) {
        Film film = filmRepository.findByIdWithCategories(filmDto.getId())
                .orElseThrow(()-> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, filmDto.getId())));

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
                .orElseThrow(()-> new EntityNotFoundException(String.format(FILM_ERROR_MESSAGE, id)));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(CATEGORY_ERROR_MESSAGE, categoryId)));
        film.addCategory(category);
        return filmMapper.toDto(film);
    }
}
