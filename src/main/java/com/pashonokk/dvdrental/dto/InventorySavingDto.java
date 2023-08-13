package com.pashonokk.dvdrental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pashonokk.dvdrental.util.CustomOffsetDateTimeDeserializer;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InventorySavingDto {
    private Long id;
    @NotNull(message = "Store id has to exist")
    @Min(value = 0, message = "Store id can`t be negative")
    private Long storeId;
    @NotNull(message = "Film id has to exist")
    @Min(value = 0, message = "Film id can`t be negative")
    private Long filmId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime lastUpdate;
}
