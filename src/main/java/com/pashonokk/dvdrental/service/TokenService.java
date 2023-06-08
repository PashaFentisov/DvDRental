package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.entity.Token;
import com.pashonokk.dvdrental.entity.User;
import com.pashonokk.dvdrental.exception.TokenExpiredException;
import com.pashonokk.dvdrental.exception.UserNotFoundException;
import com.pashonokk.dvdrental.repository.TokenRepository;
import com.pashonokk.dvdrental.util.TokenProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    private final TokenProperties tokenProperties;

    @Transactional(readOnly = true)
    public User findUserByTokenValue(String value) {
        Token tokenByValue = tokenRepository.getTokenByValue(value)
                .orElseThrow(() -> new UserNotFoundException("There isn`t user with such token " + value));
        LocalDateTime tokenExpiredTime = tokenByValue.getCreateTime().plusSeconds(tokenProperties.getDuration().getSeconds());
        if (tokenExpiredTime.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token " + value + " is no longer valid");
        }
        return tokenByValue.getUser();
    }
}
