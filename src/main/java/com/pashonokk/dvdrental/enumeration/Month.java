package com.pashonokk.dvdrental.enumeration;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Month {
    @Min(value = 0, message = "Month number can`t be less than 0")
    @Max(value = 12, message = "Month number can`t be more than 12")
    private int number;
    private List<Integer> days;
}
