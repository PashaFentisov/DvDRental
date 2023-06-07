package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.entity.Token;
import com.pashonokk.dvdrental.entity.User;
import com.pashonokk.dvdrental.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    @Transactional(readOnly = true)
    public User findUserByTokenValue(String value) {
        return tokenRepository.getTokenByValue(value)
                .map(Token::getUser)
                .orElse(null);
    }
}
