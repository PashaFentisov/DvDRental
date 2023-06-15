package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.EmailDto;

public interface NotificationService {
    void send(EmailDto emailDto);
}
