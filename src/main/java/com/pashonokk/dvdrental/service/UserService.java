package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.UserDto;
import com.pashonokk.dvdrental.dto.UserTokenDto;
import com.pashonokk.dvdrental.entity.Role;
import com.pashonokk.dvdrental.entity.Token;
import com.pashonokk.dvdrental.entity.User;
import com.pashonokk.dvdrental.event.UserRegistrationCompletedEvent;
import com.pashonokk.dvdrental.exception.UserExistsException;
import com.pashonokk.dvdrental.mapper.UserMapper;
import com.pashonokk.dvdrental.repository.RoleRepository;
import com.pashonokk.dvdrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher applicationEventPublisher;


    public void saveRegisteredUser(UserDto userDto) {
        if (userRepository.findUserIdByEmail(userDto.getEmail()) != null) {
            throw new UserExistsException("User with email " + userDto.getEmail() + " already exists");
        }
        User user = userMapper.toEntity(userDto);
        var token = new Token();
        Role roleUser = roleRepository.findRoleByName("ROLE_USER");
        user.setRole(roleUser);
        token.addUser(user);
        userRepository.save(user);
        UserTokenDto userTokenDto = new UserTokenDto(token.getValue(), user.getEmail());
        applicationEventPublisher.publishEvent(new UserRegistrationCompletedEvent(userTokenDto));
    }

    public void confirmUserEmail(String token) {
        User userByTokenValue = tokenService.validateToken(token);
        userByTokenValue.setIsVerified(true);
    }
}
