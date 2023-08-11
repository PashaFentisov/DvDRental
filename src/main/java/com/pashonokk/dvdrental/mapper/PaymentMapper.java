package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.PaymentDto;
import com.pashonokk.dvdrental.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, StaffMapper.class, RentalMapper.class})
public interface PaymentMapper {
    Payment toEntity(PaymentDto paymentDto);

    PaymentDto toDto(Payment payment);
}
