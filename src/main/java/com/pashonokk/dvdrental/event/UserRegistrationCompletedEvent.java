package com.pashonokk.dvdrental.event;

import com.pashonokk.dvdrental.dto.UserTokenDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistrationCompletedEvent {
    private UserTokenDto userTokenDto;
}
