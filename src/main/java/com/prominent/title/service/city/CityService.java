package com.prominent.title.service.city;

import com.prominent.title.dto.CityDto;
import com.prominent.title.entity.resource.City;
import com.prominent.title.exception.CityAlreadyExistsException;
import com.prominent.title.repository.CityRepository;
import com.prominent.title.utility.RecordCreationUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CityService {

    private final CityRepository cityRepository;

    private final RecordCreationUtility recordCreationUtility;

    public CityService(CityRepository cityRepository, RecordCreationUtility recordCreationUtility) {
        this.cityRepository = cityRepository;
        this.recordCreationUtility = recordCreationUtility;
    }

    /**
     * This method add city.
     *
     * @param cityDto cityDto
     * @return {@link CityDto}
     * @see CityDto
     */
    public CityDto addCityCounty(CityDto cityDto) {
        String name = WordUtils.capitalizeFully(cityDto.getName());
        if (cityRepository.existsByNameIgnoreCase(name))
            throw new CityAlreadyExistsException(cityDto.getName());
        City city = new City(name);
        BeanUtils.copyProperties(recordCreationUtility.putNewRecordInformation(), city);
        cityRepository.save(city);
        cityDto.setName(name);
        return cityDto;
    }

    /**
     * This method used to search city.
     *
     * @param searchTerm searchTerm
     * @return {@link List}
     * @see List
     * @see CityDto
     */

    public List<CityDto> searchCityCounty(String searchTerm) {
        if (!Objects.equals(searchTerm, null))
            return cityRepository.findByNameLikeIgnoreCaseOrderByNameAsc(searchTerm.toUpperCase());
        else
            return cityRepository.findAll().stream().map(City::getName).map(CityDto::new).collect(Collectors.toList());
    }
}
