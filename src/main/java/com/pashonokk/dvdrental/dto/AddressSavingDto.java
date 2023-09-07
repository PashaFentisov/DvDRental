package com.pashonokk.dvdrental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pashonokk.dvdrental.util.CustomOffsetDateTimeDeserializer;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressSavingDto {
    private Long id;
    private int houseNumber;
    private String street;
    private String district;
    @Range(min = 1000, max = 99999, message = "Postal code must be between 1000 and 99999")
    @NotNull(message = "Postal code can`t be null")
    private int postalCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @JsonDeserialize(using = CustomOffsetDateTimeDeserializer.class)
    private OffsetDateTime lastUpdate;
    @NotNull(message = "City id can`t be null")
    @Min(value = 0, message = "City id can`t be negative")
    private Long cityId;
    @AssertFalse(message = "You can`t set isDeleted as true")
    private Boolean isDeleted = false;
    @NotBlank(message = "Phone number can`t be empty or null")
    private String number;
}
