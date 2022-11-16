package com.prominent.title.service;

import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.dto.user.UserLoginDto;
import com.prominent.title.entity.user.Role;
import com.prominent.title.entity.user.User;
import com.prominent.title.entity.user.UserRoles;
import com.prominent.title.exception.UserNotFoundException;
import com.prominent.title.repository.LoginHistoryRepository;
import com.prominent.title.repository.UserRepository;
import com.prominent.title.service.auth.AuthService;
import com.prominent.title.utility.Constant;
import com.prominent.title.utility.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    User user;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private LoginHistoryRepository loginHistoryRepository;
    @InjectMocks
    private AuthService authService;

    private UserLoginDto userLoginDto;

    private Role role;

    private UserRoles userRoles;

    @BeforeEach
    public void setup() {
        userLoginDto = new UserLoginDto("dipak@gmail.com", "Test@1234567");

        user = new User();
        user.setUserName("Dipak");
        user.setUserPassword("Test@123456");
        user.setContactNumber("914455098988");
        user.setFirstName("Dipak");
        user.setLastName("Patel");
        user.setActive(true);

        role = new Role();
        role.setRoleId(1);
        role.setRoleCode("User");
        role.setCreateDate(LocalDateTime.now());

        Set<UserRoles> userRolesSet = new HashSet<>();
        userRoles = new UserRoles();
        userRoles.setRole(role);
        userRoles.setUser(user);
        userRolesSet.add(userRoles);
        user.setUserRoles(userRolesSet);
    }

    @Test
    void verify_user_login_attempts_test() {

        when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(user));

        String isLoggedInSuccess = authService.verifyUserLogonAttempts(true, userLoginDto);

        assertTrue(isLoggedInSuccess.equalsIgnoreCase("User Logged In Successfully"));
    }

    @Test
    void verify_user_login_credentials() {
        when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        GenericResponse genericResponse = authService.verifyCredentials(userLoginDto);

        assertTrue(genericResponse.getMessage().equalsIgnoreCase(Constant.USER_LOGGED_IN_SUCCESSFULLY));
    }

    @Test
    void verify_user_login_user_not_found() {
        when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(null));

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            authService.verifyCredentials(userLoginDto);
        });

        assertTrue(exception.getMessage().contains(userLoginDto.getUserName()));

    }

    @Test
    void verify_user_login_user_account_locked() {

        when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        GenericResponse genericResponse = authService.verifyCredentials(userLoginDto);

        assertTrue(genericResponse.getMessage().equalsIgnoreCase(Constant.USER_ACCOUNT_LOCKED));
    }


}
