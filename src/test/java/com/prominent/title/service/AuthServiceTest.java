package com.prominent.title.service;

import com.prominent.title.dto.user.UserLoginDto;
import com.prominent.title.entity.user.User;
import com.prominent.title.repository.UserRepository;
import com.prominent.title.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    User user;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUserName("Dipak");
        user.setUserPassword("Test@123456");
        user.setContactNumber("914455098988");
        user.setFirstName("Dipak");
        user.setLastName("Patel");
        user.setActive(true);
    }

    @Test
    void verify_user_login_attempts_test() {
        UserLoginDto userLoginDto = new UserLoginDto("dipak@gmail.com", "Test@1234567");
        when(userRepository.findByUserName(any())).thenReturn(Optional.ofNullable(user));

        String isLoggedInSuccess = authService.verifyUserLogonAttempts(true, userLoginDto);
        System.out.println("Is login success: " + isLoggedInSuccess);
        assertThat(isLoggedInSuccess.equalsIgnoreCase("User Logged In Successfully"));
    }
}
