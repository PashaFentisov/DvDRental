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

    @Transactional
    public User findUserByUuid(String uuid) {
        return tokenRepository.getTokenByUuid(uuid).map(Token::getUser).orElse(null);
    }
}
