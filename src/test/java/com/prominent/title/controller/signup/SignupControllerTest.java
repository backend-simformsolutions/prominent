package com.prominent.title.controller.signup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prominent.title.dto.user.UserSignupDto;
import com.prominent.title.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class SignupControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;

    @Test
    void userSignUpTest() throws Exception {
        UserSignupDto userSignupDto = new UserSignupDto("dipak@gmail.com", "9963725849", "User", "Test@1231111", "Test@1231111");
        Mockito.when(userService.addUser(Mockito.any(), Mockito.any())).thenReturn(userSignupDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/signup/user")
                        .content(objectMapper.writeValueAsString(userSignupDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
