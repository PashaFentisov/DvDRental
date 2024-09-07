package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.ActorDto;
import com.pashonokk.dvdrental.entity.Actor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActorMapper {

    Actor toEntity(ActorDto actorDto);
    ActorDto toDto(Actor actor);
}
