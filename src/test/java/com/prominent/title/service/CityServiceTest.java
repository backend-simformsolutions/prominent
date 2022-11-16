package com.prominent.title.service;

import com.prominent.title.dto.CityDto;
import com.prominent.title.dto.util.CreateRecordInformation;
import com.prominent.title.entity.resource.City;
import com.prominent.title.exception.CityAlreadyExistsException;
import com.prominent.title.repository.CityRepository;
import com.prominent.title.service.city.CityService;
import com.prominent.title.utility.RecordCreationUtility;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Mock
    private RecordCreationUtility recordCreationUtility;

    private City city;
    private CityDto cityDto;

    @BeforeEach
    public void setup() {
        cityDto = new CityDto("Prantij");
        city = new City("Himmatnagar");
    }

    @Test
    void add_city_county_test() {
        when(cityRepository.save(any())).thenReturn(city);
        when(recordCreationUtility.putNewRecordInformation()).thenReturn(new CreateRecordInformation(true, -1, LocalDateTime.now(), RandomString.make(30), RandomString.make(10)));

        CityDto savedCityDto = cityService.addCityCounty(cityDto);

        assertThat(savedCityDto).isNotNull();
    }

    @Test
    void add_city_county_already_exists_test() {

        when(cityRepository.existsByNameIgnoreCase(any())).thenReturn(true);

        Exception exception = assertThrows(CityAlreadyExistsException.class, () -> {
            cityService.addCityCounty(cityDto);
        });
        assertTrue(exception.getMessage().contains(cityDto.getName()));
    }

    @Test
    void search_city_county() {
        List<CityDto> cityList = new ArrayList<>();
        cityList.add(new CityDto("Prantij"));
        cityList.add(new CityDto("Himmatnagar"));
        cityList.add(new CityDto("Satlasana"));
        when(cityRepository.findByNameLikeIgnoreCaseOrderByNameAsc(any())).thenReturn(cityList);

        List<CityDto> fetchedCity = cityService.searchCityCounty("Prantij");
        assertThat(fetchedCity).isNotEmpty();
    }
}
