package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.entity.Role;
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
    public User save(User user){
        Role roleUser = roleRepository.findRoleByName("ROLE_USER");
        roleUser.getUsers().add(user);
//        user.getRoles().add(roleUser);
//        roleUser.setUsers(List.of(user));
        user.setRoles(List.of(roleUser));
        return userRepository.save(user);
    }

    public String generateTokenForUser(User user){
        return UUID.nameUUIDFromBytes(user.toString().getBytes()).toString();
    }   //цей викликати в сейв зробити нову міграцію доадати нове поле до юзера і в емайл так звіряти
}
