package com.prominent.title.service;

import com.prominent.title.dto.CityDto;
import com.prominent.title.entity.resource.City;
import com.prominent.title.repository.CityRepository;
import com.prominent.title.service.city.CityService;
import com.prominent.title.utility.RecordCreationUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @InjectMocks
    private RecordCreationUtility recordCreationUtility;

    private City city;

    @BeforeEach
    public void setup() {
        city = new City("Himmatnagar");
    }

    @Test
    void add_city_county_test() {
        when(cityRepository.save(city)).thenReturn(city);
        City savedCity = cityRepository.save(city);
        assertThat(savedCity).isNotNull();
    }

    @Test
    void search_city_county() {
        List<CityDto> cityList = new ArrayList<>();
        cityList.add(new CityDto("Prantij"));
        cityList.add(new CityDto("Himmatnagar"));
        cityList.add(new CityDto("Satlasana"));
        when(cityRepository.findByNameLikeIgnoreCaseOrderByNameAsc(any())).thenReturn(cityList);

        List<CityDto> fetchedCity = cityService.searchCityCounty("Prantij");
        assertThat(fetchedCity.size()).isPositive();
    }
}
