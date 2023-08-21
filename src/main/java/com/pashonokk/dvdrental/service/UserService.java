package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.entity.Customer;
import com.pashonokk.dvdrental.entity.Role;
import com.pashonokk.dvdrental.entity.Token;
import com.pashonokk.dvdrental.entity.User;
import com.pashonokk.dvdrental.event.UserRegistrationCompletedEvent;
import com.pashonokk.dvdrental.exception.AuthenticationException;
import com.pashonokk.dvdrental.exception.UserExistsException;
import com.pashonokk.dvdrental.mapper.UserCustomerSavingMapper;
import com.pashonokk.dvdrental.repository.RoleRepository;
import com.pashonokk.dvdrental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final CustomerService customerService;
    private final UserCustomerSavingMapper userMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public void saveRegisteredUser(UserCustomerSavingDto userDto) {
        if (userRepository.findUserIdByEmail(userDto.getEmail()) != null) {
            throw new UserExistsException("User with email " + userDto.getEmail() + " already exists");
        }
        User user = userMapper.toEntity(userDto);
        Customer customer = constructAndSaveCustomer(userDto);
        customer.addUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roleUser = roleRepository.findRoleByName("ROLE_CUSTOMER");
        user.setRole(roleUser);
        Token token = new Token();
        token.addUser(user);
        userRepository.save(user);
        EmailDto emailDto = createEmailDto(user.getEmail(), token.getValue());
        applicationEventPublisher.publishEvent(new UserRegistrationCompletedEvent(emailDto));
    }

    private Customer constructAndSaveCustomer(UserCustomerSavingDto userDto) {
        return customerService.addCustomer(userDto.getAddress());
    }

    public JwtAuthorizationResponse authorize(UserAuthorizationDto userDto) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Email or password is wrong, try again");
        }
        User user = userRepository.findUserByEmail(userDto.getEmail())
                .orElseThrow(()->new UsernameNotFoundException("User with email " + userDto.getEmail() + " doesn`t exist"));
        String token = jwtService.generateToken(user);
        OffsetDateTime expiresAt = jwtService.getExpiration(token);
        List<String> permissions = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return new JwtAuthorizationResponse(new AuthorizationToken(token, expiresAt), user.getRole().getName(), permissions);
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
