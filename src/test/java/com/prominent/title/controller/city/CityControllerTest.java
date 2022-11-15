package com.prominent.title.controller.city;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prominent.title.dto.CityDto;
import com.prominent.title.service.city.CityService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class CityControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CityService cityService;

    @Test
    void addCityTest() throws Exception {
        CityDto cityDto = new CityDto("Test");
        Mockito.when(cityService.addCityCounty(Mockito.any())).thenReturn(cityDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add")
                        .content(objectMapper.writeValueAsString(cityDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void findAllCountyTest() throws Exception {
        List<CityDto> cityDtoList = new ArrayList<>();
        cityDtoList.add(new CityDto("City1"));
        cityDtoList.add(new CityDto("City2"));
        Mockito.when(cityService.searchCityCounty(Mockito.any())).thenReturn(cityDtoList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/city/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addCountiesTest() throws Exception {

        List<String> cityList = Arrays.asList("City1", "City2");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add-multiple")
                        .content(objectMapper.writeValueAsString(cityList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
