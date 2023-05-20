package com.pashonokk.dvdrental.apiInformation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Param {
    private String name;
    private boolean isRequired;
}