package com.pashonokk.dvdrental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pashonokk.dvdrental.util.CustomOffsetDateTimeDeserializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StaffSavingDto {
    private Long id;
    @Valid
    @NotNull(message = "Staff`s contact info can`t be null")
    private ContactInfoDto contactInfo;
    private String pictureUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime lastUpdate;
    @Valid
    @NotNull(message = "Staff`s address can`t be null")
    private AddressSavingDto addressSavingDto;
    @NotNull(message = "Store id can`t be empty or null")
    @Min(value = 0, message = "Id can`t be negative")
    private Long storeId;
    @AssertFalse(message = "You can`t set isDeleted as true")
    private Boolean isDeleted = false;

}
