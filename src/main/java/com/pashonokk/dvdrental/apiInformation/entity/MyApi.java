package com.pashonokk.dvdrental.apiInformation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyApi {
    private int totalNumberOfAPIs;
    private List<ControllerRecord> records;
}
