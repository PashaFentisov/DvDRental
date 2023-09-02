package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    boolean existsByEmail(String email);
    @Query("SELECT u FROM User u left join fetch u.role left join fetch u.token WHERE u.email = :email")
    Optional<User> findUserByEmail(String email);
}
