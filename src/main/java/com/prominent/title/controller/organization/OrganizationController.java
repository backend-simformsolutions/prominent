package com.prominent.title.controller.organization;

import com.prominent.title.dto.organization.OrganizationDto;
import com.prominent.title.dto.organization.OrganizationDtoWithBrokers;
import com.prominent.title.dto.organization.OrganizationEntryDto;
import com.prominent.title.dto.organization.OrganizationSearchDto;
import com.prominent.title.dto.response.GenericFilterDto;
import com.prominent.title.dto.response.GenericListResponse;
import com.prominent.title.dto.response.GenericPageableDto;
import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.repository.OrganizationRepository;
import com.prominent.title.service.organization.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RestController
@RequestMapping("/organization")
@Slf4j
public class OrganizationController {

    private final OrganizationService organizationService;

    private final OrganizationRepository organizationRepository;

    public OrganizationController(OrganizationService organizationService, OrganizationRepository organizationRepository) {
        this.organizationService = organizationService;
        this.organizationRepository = organizationRepository;
    }

    /**
     * This method accepts organization data with validation  And gives warnings for particular fields which are not satisfying conditions
     *
     * @param organizationEntryDto organizationEntryDto
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Saves Organization Information", description = "This Api saves organization data and warns if organization name already exists ", tags = {"OrganizationController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = "Save User successfully", content = @Content(schema = @Schema(implementation = OrganizationDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = "Bad Request"),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = "409", description = "Organization already exists"),
            @ApiResponse(responseCode = "401", description = "Unauthorized (check Bearer Token)")
    })
    @PostMapping("/entry")
    public ResponseEntity<GenericResponse> addOrganization(@Valid @RequestBody OrganizationEntryDto organizationEntryDto) {
        log.info("Inside add organization : {} ", organizationEntryDto.getOrganizationDto().getOrganizationName());
        return new ResponseEntity<>(new GenericResponse(true, "Record Added Successfully", organizationService.add(organizationEntryDto), HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * This method retrieves organization data with validation  And gives another message if it doesn't exist
     *
     * @param id id
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Get Organization Data API", description = "This Api fetches information about organization using communication email as request parameter", tags = {"OrganizationController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = SAVE_ORGANIZATION_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = OrganizationDtoWithBrokers.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = NOT_FOUND_CODE, description = ORGANIZATION_NOT_FOUND),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN)
    })
    @GetMapping("/get")
    public ResponseEntity<GenericResponse> getOrganization(@RequestParam("organizationId") int id) {
        log.info("Getting Organization Information Associated With This ID {}...", id);
        return new ResponseEntity<>(new GenericResponse(true, "Record Retrieved Successfully", organizationService.getOrganization(id), HttpStatus.OK.value()), HttpStatus.OK);
    }

    /**
     * This API finds all organization and returns list of organization dto  Which contains information like organization name, manager's name, email and contact
     *
     * @param columnName columnName
     * @param size       size
     * @param page       page
     * @param order      order
     * @param searchTerm searchTerm
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericListResponse
     */
    @Operation(summary = "Get All Organization Information", description = "This Api takes Params(columnName,size,page,order) and pagination and sorting is based on the supplied fields. " +
            "Order values can be ASC/DESC. Page starts from 1. By default if column is null then sorted by id, if order is null then sorted ascending and size is null then 10.", tags = {"OrganizationController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = LIST_FETCHED_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = GenericListResponse.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN)
    })
    @GetMapping("/list")
    public ResponseEntity<GenericListResponse> getAllOrganizationsPageable(
            @Parameter(description = "The type of columns", schema = @Schema(allowableValues = {"organizationId", "organizationName", "city", "state", "address", "primaryContactName", "primaryContactPhone", "primaryContactEmail"}))
            @RequestParam(value = "columnName", defaultValue = "organizationId") String columnName,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "The type of orders", schema = @Schema(allowableValues = {"ASC", "DESC"}))
            @RequestParam(value = "order", defaultValue = "ASC") String order,
            @RequestParam(value = "searchTerm", required = false) String searchTerm) {
        log.info("Getting All Organization Information ...");
        return new ResponseEntity<>(organizationService.findAllOrganizationPageable(new GenericPageableDto(columnName, size, page, order), new GenericFilterDto(searchTerm)), HttpStatus.OK);
    }

    /**
     * This API takes organizationEntryDto which contains user information and user's payment information  and updates organization information if it exists else throws exception
     *
     * @param organizationEntryDto organizationEntryDto
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Update Organization Details API", description = "This API takes fields which need to be updated and return the updated organization Details", tags = {"OrganizationController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = UPDATE_ORGANIZATION_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = OrganizationDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN)
    })
    @PutMapping("/edit")
    public ResponseEntity<GenericResponse> editOrganization(@RequestBody OrganizationEntryDto organizationEntryDto) {
        log.info("updating organization {}...", organizationEntryDto.getOrganizationDto().getOrganizationName());
        GenericResponse genericResponse = new GenericResponse(true, "Organization Edited Successfully", organizationService.editOrganizationDetails(organizationEntryDto), HttpStatus.OK.value());
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    /**
     * This API deletes Organization using field organizationId.
     *
     * @param organizationId organizationId
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Delete Organization Details API", description = "This API delete all records of organization from the database", tags = {"OrganizationController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = DELETE_ORGANIZATION_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = GenericResponse.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<GenericResponse> deleteOrganization(@RequestParam int organizationId) {
        GenericResponse genericResponse = new GenericResponse(true, "Organization Deleted Successfully", organizationService.deleteOrganization(organizationId), HttpStatus.OK.value());
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    /**
     * This API used to search organization with organization name.
     *
     * @param searchTerm searchTerm
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */


    @Operation(summary = "Search Organization", description = "In this organization is searched using organization name", tags = {"OrganizationController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = SEARCH_ORGANIZATION_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = OrganizationSearchDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED)
    })
    @GetMapping("/search")
    public ResponseEntity<GenericResponse> searchOrganization(@RequestParam(name = "searchTerm") String searchTerm) {
        log.info("Getting Organizations Information Associated With This search parameter {}...", searchTerm);
        List<OrganizationSearchDto> organizationSearchDtos = organizationService.searchOrganization(searchTerm);
        GenericResponse genericResponse = new GenericResponse(true, organizationSearchDtos.isEmpty() ? "No Record Found" : "Record Fetched Successfully", organizationSearchDtos, HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(genericResponse);
    }
}
