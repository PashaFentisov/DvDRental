package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface CustomerMapper {
    Customer toEntity(CustomerDto customerDto);

    CustomerDto toDto(Customer customer);
}
