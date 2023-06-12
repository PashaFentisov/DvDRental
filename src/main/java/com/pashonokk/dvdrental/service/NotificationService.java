package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.UserTokenDto;

public interface NotificationService {
    void send(UserTokenDto userTokenDto);
}
