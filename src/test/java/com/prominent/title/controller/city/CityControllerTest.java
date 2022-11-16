package com.prominent.title.controller.city;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prominent.title.dto.CityDto;
import com.prominent.title.exception.CityAlreadyExistsException;
import com.prominent.title.service.city.CityService;
import org.junit.jupiter.api.Test;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
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
    void add_city_test_200_ok() throws Exception {
        CityDto cityDto = new CityDto("Prantij");
        when(cityService.addCityCounty(any())).thenReturn(cityDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add")
                        .content(objectMapper.writeValueAsString(cityDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void add_city_test_400_not_found() throws Exception {
        CityDto cityDto = new CityDto("Prantij");
        when(cityService.addCityCounty(any())).thenThrow(CityAlreadyExistsException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add")
                        .content(objectMapper.writeValueAsString(cityDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void find_all_county_test_200_ok() throws Exception {
        List<CityDto> cityDtoList = new ArrayList<>();
        cityDtoList.add(new CityDto("Prantij"));
        cityDtoList.add(new CityDto("Himmatnagar"));
        when(cityService.searchCityCounty(any())).thenReturn(cityDtoList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/city/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void add_counties_test_200_ok() throws Exception {

        List<String> cityList = Arrays.asList("Prantij", "Himmatnagar");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/city/add-multiple")
                        .content(objectMapper.writeValueAsString(cityList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
