package com.pashonokk.dvdrental.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pashonokk.dvdrental.annotation.ValidOffsetDateTime;
import com.pashonokk.dvdrental.util.CustomOffsetDateTimeDeserializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Customer`s first name can`t be empty or null")
    private String firstName;
    @NotBlank(message = "Customer`s last name can`t be empty or null")
    private String lastName;
    @NotBlank(message = "Customer`s email can`t be empty or null")
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime lastUpdate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    @ValidOffsetDateTime
    private OffsetDateTime createDate;
    @Valid
    @NotNull
    private AddressSavingDto addressSavingDto;
    private boolean active;
}
