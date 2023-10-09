package com.pashonokk.dvdrental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pashonokk.dvdrental.util.CustomOffsetDateTimeDeserializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreSavingDto {
    private Long id;
    @Valid
    @NotNull(message = "Store`s address can`t be null")
    private AddressSavingDto addressSavingDto;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime lastUpdate;
    @AssertFalse(message = "You can`t set isDeleted as true")
    private Boolean isDeleted = false;
}
