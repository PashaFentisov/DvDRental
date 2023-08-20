package com.pashonokk.dvdrental.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pashonokk.dvdrental.annotation.ValidOffsetDateTime;
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
public class CustomerSavingDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime lastUpdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    @ValidOffsetDateTime
    private OffsetDateTime createDate;
    @Valid
    @NotNull(message = "Customer`s contact info can`t be null")
    private ContactInfoDto contactInfo;
    @Valid
    @NotNull(message = "Customer`s address can`t be null")
    private AddressSavingDto address;
    @AssertFalse(message = "You can`t set isDeleted as true")
    private Boolean isDeleted = false;

}
