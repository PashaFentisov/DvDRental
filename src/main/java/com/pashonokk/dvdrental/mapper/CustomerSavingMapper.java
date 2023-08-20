package com.pashonokk.dvdrental.mapper;

import com.pashonokk.dvdrental.dto.CustomerSavingDto;
import com.pashonokk.dvdrental.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AddressSavingMapper.class, ContactInfoMapper.class})
public interface CustomerSavingMapper {
    Customer toEntity(CustomerSavingDto customerSavingDto);
    CustomerSavingDto toDto(Customer customer);
}
