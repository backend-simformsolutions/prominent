package com.prominent.title.repository;

import com.prominent.title.dto.CityDto;
import com.prominent.title.entity.resource.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {

    @Query("select new com.prominent.title.dto.CityDto(c.name) from City c where upper(c.name) like %?1% order by c.name")
    List<CityDto> findByNameLikeIgnoreCaseOrderByNameAsc(String name);

    @Query("select (count(c) > 0) from City c where lower(c.name) = lower(?1)")
    boolean existsByNameIgnoreCase(String name);


}