package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.EmailDto;
import com.pashonokk.dvdrental.dto.UserDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public String saveRegisteredUser(UserDto userDto) {
        if (userRepository.findUserIdByEmail(userDto.getEmail()) != null) {
            throw new UserExistsException("User with email " + userDto.getEmail() + " already exists");
        }
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roleUser = roleRepository.findRoleByName("ROLE_USER");
        user.setRole(roleUser);
        Token token = new Token();
        token.addUser(user);
        User savedUser = userRepository.save(user);
        String generatedToken = jwtService.generateToken(savedUser);
        EmailDto emailDto = createEmailDto(user.getEmail(), token.getValue());
        applicationEventPublisher.publishEvent(new UserRegistrationCompletedEvent(emailDto));
        return generatedToken;
    }

    public String signIn(UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        User user = userRepository.findUserByEmail(userDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("User with email " + userDto.getEmail() + " doesn`t exist"));
        return jwtService.generateToken(user);
    }

    public void confirmUserEmail(String token) {
        User userByTokenValue = tokenService.validateToken(token);
        userByTokenValue.setIsVerified(true);
    }

    private EmailDto createEmailDto(String userEmail, String tokenValue) {
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(userEmail);
        emailDto.setBody(tokenValue);
        emailDto.setSubject("Follow this link to confirm your email");
        return emailDto;
    }

}
