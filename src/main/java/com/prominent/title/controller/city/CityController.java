package com.prominent.title.controller.city;

import com.prominent.title.dto.CityDto;
import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.service.city.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.prominent.title.constants.Constant.*;

@Slf4j
@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService resourceService;

    public CityController(CityService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * This API used to add city
     *
     * @param cityDto cityDto
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */

    @Operation(summary = "Add County", description = ADD_COUNTRY, tags = {"City"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = ADD_COUNTRY, content = @Content(schema = @Schema(implementation = CityDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = NOT_FOUND_CODE, description = NOT_FOUND)
    })
    @PostMapping("/add")
    public ResponseEntity<GenericResponse> addCityCounty(@Valid @RequestBody CityDto cityDto) {
        GenericResponse genericResponse = new GenericResponse(true, "County Added Successfully", resourceService.addCityCounty(cityDto), HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.valueOf(genericResponse.getCode())).body(genericResponse);
    }

    /**
     * This Api used to fetch countries with search.
     *
     * @param searchTerm searchTerm
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */

    @Operation(summary = "List Counties With Search", description = "Search Counties using searchTerm as Param", tags = {"City"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = ADD_COUNTRY, content = @Content(schema = @Schema(implementation = CityDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = NOT_FOUND_CODE, description = NOT_FOUND)
    })
    @GetMapping("/list")
    public ResponseEntity<GenericResponse> findAllCounty(@RequestParam(value = "searchTerm", required = false) String searchTerm) {
        List<CityDto> cityDtoList = resourceService.searchCityCounty(searchTerm);
        GenericResponse genericResponse = new GenericResponse(true, cityDtoList.isEmpty() ? "Record Not Found" : "Record Retrieved Successfully", cityDtoList, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.valueOf(genericResponse.getCode())).body(genericResponse);
    }

    /**
     * This API used to add countries.
     *
     * @param cities cities
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Add Counties", description = "Add County", tags = {"City"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = ADD_COUNTRY, content = @Content(schema = @Schema(implementation = CityDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = NOT_FOUND_CODE, description = NOT_FOUND)
    })
    @PostMapping("/add-multiple")
    public ResponseEntity<GenericResponse> addCounties(@RequestBody List<String> cities) {
        cities.forEach(s -> resourceService.addCityCounty(new CityDto(s)));
        GenericResponse genericResponse = new GenericResponse(true, "County Added Successfully", cities, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.valueOf(genericResponse.getCode())).body(genericResponse);
    }
}
