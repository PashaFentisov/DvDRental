package com.pashonokk.dvdrental.event;

import com.pashonokk.dvdrental.dto.EmailDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistrationCompletedEvent {
    private EmailDto emailDto;
}
