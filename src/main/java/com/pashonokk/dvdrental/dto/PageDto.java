package com.pashonokk.dvdrental.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {
    private int page = 0;
    private int size = 10;
    private String sort = "id";
}
