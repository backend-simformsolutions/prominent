package com.prominent.title.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prominent.title.dto.user.UserDetailsDto;
import com.prominent.title.exception.UserNotFoundException;
import com.prominent.title.repository.UserRepository;
import com.prominent.title.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    void edit_user_test_200_ok() throws Exception {

        UserDetailsDto userDetailsDto = new UserDetailsDto(1, "Dipak", "Dipak", "Patel", "null", "null", "null", "null", "null", "null", "null");
        when(userService.editUserDetails(any())).thenReturn(userDetailsDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/edit")
                        .content(objectMapper.writeValueAsString(userDetailsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void edit_user_test_404_not_found() throws Exception {

        UserDetailsDto userDetailsDto = new UserDetailsDto(1, "Dipak", "Dipak", "Patel", "null", "null", "null", "null", "null", "null", "null");
        when(userService.editUserDetails(any())).thenThrow(UserNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/user/edit")
                        .content(objectMapper.writeValueAsString(userDetailsDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

}


