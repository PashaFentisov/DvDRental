package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.ActorDto;
import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.entity.Actor;
import com.pashonokk.dvdrental.mapper.ActorMapper;
import com.pashonokk.dvdrental.mapper.FilmMapper;
import com.pashonokk.dvdrental.mapper.PageMapper;
import com.pashonokk.dvdrental.repository.ActorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;
    private final PageMapper pageMapper;
    private final ActorMapper actorMapper;
    private final FilmMapper filmMapper;
    private static final String ERROR_MESSAGE = "Actor with id %s doesn't exist";

    @Transactional(readOnly = true)
    public PageResponse<ActorDto> getAllActors(Pageable pageable) {
        return pageMapper.toPageResponse(actorRepository.findAll(pageable).map(actorMapper::toDto));
    }

    @Transactional(readOnly = true)
    public ActorDto getActorById(Long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        return actorMapper.toDto(actor);
    }
    @Transactional(readOnly = true)
    public List<FilmDto> getFilmsByActorId(Long id) {
        Actor actor = actorRepository.findByIdWithFilms(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        return actor.getFilms().stream().map(filmMapper::toDto).toList();
    }

    @Transactional
    public ActorDto addActor(ActorDto actorDto) {
        Actor actor = actorMapper.toEntity(actorDto);
        actor.setIsDeleted(false);
        actor.setLastUpdate(OffsetDateTime.now());
        Actor savedActor = actorRepository.save(actor);
        return actorMapper.toDto(savedActor);
    }
    @Transactional
    public ActorDto updateSomeFieldsOfActor(ActorDto actorDto) {
        Actor actor = actorRepository.findById(actorDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, actorDto.getId())));
        Optional.ofNullable(actorDto.getBiography()).ifPresent(actor::setBiography);
        return actorMapper.toDto(actor);
    }
    @Transactional
    public void deleteActorById(Long id) {
        Actor actor = actorRepository.findByIdWithFilms(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ERROR_MESSAGE, id)));
        actor.setIsDeleted(true);
    }
}
