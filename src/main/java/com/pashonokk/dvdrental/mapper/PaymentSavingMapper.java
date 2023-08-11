package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.PaymentSavingDto;
import com.pashonokk.dvdrental.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentSavingMapper {
    Payment toEntity(PaymentSavingDto paymentSavingDtoDto);

    PaymentSavingDto toDto(Payment payment);
}
