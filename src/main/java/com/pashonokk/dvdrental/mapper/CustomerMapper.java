package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CustomerDto;
import com.pashonokk.dvdrental.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface CustomerMapper {
    @Mapping(source = "addressDto", target = "address")
    Customer toEntity(CustomerDto customerDto);
    @Mapping(source = "address", target = "addressDto")
    CustomerDto toDto(Customer customer);
}
