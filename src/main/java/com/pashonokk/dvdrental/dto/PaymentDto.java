package com.pashonokk.dvdrental.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private BigDecimal amount;
    private OffsetDateTime paymentDate;
//    private CustomerDto customer; //todo
//    private StaffDto staff;
//    private RentalDto rental;
    private Boolean isClosed;

}
