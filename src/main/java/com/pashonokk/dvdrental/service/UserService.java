package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.entity.Role;
import com.pashonokk.dvdrental.entity.Token;
import com.pashonokk.dvdrental.entity.User;
import com.pashonokk.dvdrental.repository.RoleRepository;
import com.pashonokk.dvdrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User save(User user) {
        var token = generateTokenForUser(user);
        Role roleUser = roleRepository.findRoleByName("ROLE_USER");
        user.setToken(token);
        token.setUser(user);
        roleUser.getUsers().add(user);
        user.setRoles(List.of(roleUser));
        return userRepository.save(user);
    }

    private Token generateTokenForUser(User user) {
        Token token = new Token();
        token.setUuid(UUID.nameUUIDFromBytes(user.toString().getBytes()).toString());
        return token;
    }

    @Transactional
    public void setUserAsVerified(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setVerified(true);
    }
}
