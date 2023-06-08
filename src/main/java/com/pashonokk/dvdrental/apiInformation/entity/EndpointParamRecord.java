package com.pashonokk.dvdrental.apiInformation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointParamRecord {
    private String name;
    private boolean isRequired;
}