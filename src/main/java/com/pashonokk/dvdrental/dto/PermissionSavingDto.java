package com.pashonokk.dvdrental.dto;

import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PermissionSavingDto {
    private Long id;
    @NotBlank(message = "Description can`t be empty or null")
    private String name;

    @NotNull(message = "The list of roleIds can not be null")
    @NotEmpty(message = "The list of roleIds must not be empty")
    private Set<Long> roleIds;
    @AssertFalse(message = "You can`t set isDeleted as true")
    private Boolean isDeleted = false;
}
