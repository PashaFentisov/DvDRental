package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> getTokenByValue(String value);

    @Query("SELECT t.value FROM Token t where t.userId = (Select u.id from User u where u.email = :email)")
    String findTokenByUserEmail(String email);
}
