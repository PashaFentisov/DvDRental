package com.pashonokk.dvdrental.repository;

import com.pashonokk.dvdrental.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Long, Authority> {
}
