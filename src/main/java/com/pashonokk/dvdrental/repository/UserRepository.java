package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
}
