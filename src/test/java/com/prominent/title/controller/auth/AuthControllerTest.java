package com.prominent.title.controller.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prominent.title.dto.EmailDto;
import com.prominent.title.dto.LoginResponseDto;
import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.dto.user.UserLoginDto;
import com.prominent.title.entity.user.User;
import com.prominent.title.service.auth.AuthService;
import com.prominent.title.utility.Constant;
import com.prominent.title.utility.JwtUtil;
import com.prominent.title.validation.ResetPasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Mock
    ResetPasswordValidator resetPasswordValidator;

    @Autowired
    JwtUtil jwtUtil;

    @MockBean
    AuthService authService;

    private String jwtToken;
    private User user;

    @BeforeEach
    public void setup() {
        jwtToken = jwtUtil.generateToken("dipak@test.com", Constant.JWT_TOKEN_EXPIRATION_DURATION);

        user = new User();
        user.setUserName("Dipak");
        user.setUserPassword("Test@123456");
        user.setContactNumber("914455098988");
        user.setFirstName("Dipak");
        user.setLastName("Patel");
        user.setActive(true);
    }

    @Test
    void user_login_test_200_ok() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("dipak@test.com", "Dipak1111");
        List<String> roles = new ArrayList<>();
        roles.add("Admin");
        LoginResponseDto loginResponseDto = new LoginResponseDto(new User(), null, roles);

        when(authService.verifyCredentials(userLoginDto)).thenReturn(new GenericResponse(true, "User Logged in Successfully", loginResponseDto, HttpStatus.OK.value()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .content(objectMapper.writeValueAsString(userLoginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void user_login_test_401_bad_request() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("dipak@test.com", "Dipak1111");
        List<String> roles = new ArrayList<>();
        roles.add("Admin");
        LoginResponseDto loginResponseDto = new LoginResponseDto(new User(), jwtToken, roles);

        when(authService.verifyCredentials(userLoginDto)).thenReturn(new GenericResponse(false, "Invalid Username or password", loginResponseDto, HttpStatus.UNAUTHORIZED.value()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/login")
                        .content(objectMapper.writeValueAsString(userLoginDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void forgot_password_test_200_Ok() throws Exception {
        EmailDto emailDto = new EmailDto("dipak@test.com");

        when(authService.createPasswordResetTokenForUser(emailDto.getUserName())).thenReturn(new GenericResponse(true, "We have sent a reset password link to your email. Please check.", emailDto.getUserName(), HttpStatus.OK.value()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/forgot-password")
                        .content(objectMapper.writeValueAsString(emailDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void forgot_password_test_404_not_found() throws Exception {
        EmailDto emailDto = new EmailDto("dipak@test.com");

        when(authService.createPasswordResetTokenForUser(emailDto.getUserName())).thenReturn(new GenericResponse(false, "Cannot Find User", emailDto.getUserName(), HttpStatus.NOT_FOUND.value()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/forgot-password")
                        .content(objectMapper.writeValueAsString(emailDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void logout_test() throws Exception {
        Mockito.doNothing().when(authService).logout(new MockHttpServletRequest());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/auth/logout"))
                .andExpect(status().isOk());
    }
}

