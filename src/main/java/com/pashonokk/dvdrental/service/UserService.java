package com.pashonokk.dvdrental.service;

import com.pashonokk.dvdrental.dto.*;
import com.pashonokk.dvdrental.entity.*;
import com.pashonokk.dvdrental.event.UserRegistrationCompletedEvent;
import com.pashonokk.dvdrental.exception.AuthenticationException;
import com.pashonokk.dvdrental.exception.UserExistsException;
import com.pashonokk.dvdrental.mapper.CustomerMapper;
import com.pashonokk.dvdrental.mapper.StaffMapper;
import com.pashonokk.dvdrental.mapper.UserCustomerSavingMapper;
import com.pashonokk.dvdrental.mapper.UserStaffSavingMapper;
import com.pashonokk.dvdrental.repository.RoleRepository;
import com.pashonokk.dvdrental.repository.UserRepository;
import com.pashonokk.dvdrental.util.SendGridProperties;
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
    private final StaffService staffService;
    private final UserCustomerSavingMapper userCustomerSavingMapper;
    private final UserStaffSavingMapper userStaffSavingMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StaffMapper staffMapper;
    private final CustomerMapper customerMapper;

    private final SendGridProperties sendGridProperties;

    private static final String USER_EXISTS_ERROR_MESSAGE = "User with email %s already exists";



    public CustomerDto saveRegisteredCustomerUser(UserCustomerSavingDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserExistsException(String.format(USER_EXISTS_ERROR_MESSAGE, userDto.getEmail()));
        }
        User user = userCustomerSavingMapper.toEntity(userDto);
        Customer customer = customerService.constructCustomer(userDto.getAddress());
        customer.addUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roleUser = roleRepository.findRoleByName("ROLE_CUSTOMER");
        user.setRole(roleUser);
        Token token = new Token();
        token.addUser(user);
        userRepository.save(user);
        EmailDto emailDto = createEmailDto(user.getEmail(), token.getValue());
        applicationEventPublisher.publishEvent(new UserRegistrationCompletedEvent(emailDto));
        return customerMapper.toDto(user.getCustomer());
    }

    public StaffDto saveRegisteredStaffUser(UserStaffSavingDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new UserExistsException(String.format(USER_EXISTS_ERROR_MESSAGE, userDto.getEmail()));
        }
        User user = userStaffSavingMapper.toEntity(userDto);
        Staff staff = staffService.constructStaff(userDto);
        staff.addUser(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role roleUser = roleRepository.findRoleByName("ROLE_STAFF");
        user.setRole(roleUser);
        Token token = new Token();
        token.addUser(user);
        userRepository.save(user);
        EmailDto emailDto = createEmailDto(user.getEmail(), token.getValue());
        applicationEventPublisher.publishEvent(new UserRegistrationCompletedEvent(emailDto));
        return staffMapper.toDto(user.getStaff());
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
        return EmailDto.builder()
                .from(sendGridProperties.getEmailFrom())
                .to(userEmail)
                .body(tokenValue)
                .subject("Follow this link to confirm your email")
                .build();
    }

}
