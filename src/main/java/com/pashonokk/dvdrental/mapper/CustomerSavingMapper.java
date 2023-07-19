package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CustomerSavingDto;
import com.pashonokk.dvdrental.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AddressSavingMapper.class)
public interface CustomerSavingMapper {
//    @Mapping(target = "customer", ignore = true)
    @Mapping(source = "addressSavingDto", target = "address")
    Customer toEntity(CustomerSavingDto customerSavingDto);
    @Mapping(source = "address", target = "addressSavingDto")
    CustomerSavingDto toDto(Customer customer);
}
