package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {
    private Long id;
    private String name;
    private Set<String> roleNames;
    private Boolean isDeleted;
}
