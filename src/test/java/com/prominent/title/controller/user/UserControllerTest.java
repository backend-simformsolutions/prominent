package com.prominent.title.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prominent.title.dto.user.UserDetailsDto;
import com.prominent.title.repository.UserRepository;
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
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Test
    void editUserTest() throws Exception {

        UserDetailsDto userDetailsDto = new UserDetailsDto(1, "Dipak", "Dipak", "Patel", "null", "null", "null", "null", "null", "null", "null");
        Mockito.when(userService.editUserDetails(Mockito.any())).thenReturn(userDetailsDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/edit")
                        .content(objectMapper.writeValueAsString(userDetailsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}


