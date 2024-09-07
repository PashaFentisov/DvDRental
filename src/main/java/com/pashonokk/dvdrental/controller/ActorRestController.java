package com.pashonokk.dvdrental.controller;

import com.pashonokk.dvdrental.dto.ActorDto;
import com.pashonokk.dvdrental.dto.FilmDto;
import com.pashonokk.dvdrental.endpoint.PageResponse;
import com.pashonokk.dvdrental.exception.BigSizeException;
import com.pashonokk.dvdrental.exception.EntityValidationException;
import com.pashonokk.dvdrental.service.ActorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/actors")
@RequiredArgsConstructor
public class ActorRestController {
    private final ActorService actorService;
    private final Logger logger = LoggerFactory.getLogger(ActorRestController.class);

    @GetMapping
    public ResponseEntity<PageResponse<ActorDto>> getActors(@RequestParam(required = false, defaultValue = "0") int page,
                                                            @RequestParam(required = false, defaultValue = "10") int size,
                                                            @RequestParam(required = false, defaultValue = "id") String sort) {
        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 actors at one time");
        }
        PageResponse<ActorDto> allActors = actorService.getAllActors(PageRequest.of(page, size, Sort.by(sort)));
        return ResponseEntity.ok(allActors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActorDto> getActorById(@PathVariable Long id) {
        ActorDto actorDto = actorService.getActorById(id);
        return ResponseEntity.ok(actorDto);
    }

    @GetMapping("/{id}/films")
    public ResponseEntity<List<FilmDto>> getFilmsByActorId(@PathVariable Long id) {
        List<FilmDto> filmsByActorId = actorService.getFilmsByActorId(id);
        return ResponseEntity.ok(filmsByActorId);
    }

    @PostMapping
    public ResponseEntity<ActorDto> addActor(@RequestBody @Valid ActorDto actorDto, Errors errors) {
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(er->logger.error(er.getDefaultMessage()));
            throw new EntityValidationException("Validation failed", errors);
        }
        ActorDto savedActor = actorService.addActor(actorDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedActor.getId())
                .toUri();
        return ResponseEntity.created(location).body(savedActor);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ActorDto> updateActor(@PathVariable Long id, @RequestBody ActorDto actorDto) {
        actorDto.setId(id);
        ActorDto updatedActorDto = actorService.updateSomeFieldsOfActor(actorDto);
        return ResponseEntity.ok(updatedActorDto);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.pashonokk.dvdrental.enumeration.Permissions).DELETE_ACCESS)")
    public ResponseEntity<Object> deleteActor(@PathVariable Long id) {
        actorService.deleteActorById(id);
        return ResponseEntity.noContent().build();
    }
}
