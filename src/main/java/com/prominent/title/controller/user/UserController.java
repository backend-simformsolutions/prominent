package com.prominent.title.controller.user;

import com.prominent.title.dto.EmailDto;
import com.prominent.title.dto.response.EmptyJsonBody;
import com.prominent.title.dto.response.GenericListResponse;
import com.prominent.title.dto.response.GenericPageableDto;
import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.dto.user.EditUserDto;
import com.prominent.title.dto.user.UserDetailsDto;
import com.prominent.title.dto.user.UserEntryDto;
import com.prominent.title.dto.user.UserListDto;
import com.prominent.title.entity.user.User;
import com.prominent.title.exception.UserNotFoundException;
import com.prominent.title.repository.UserRepository;
import com.prominent.title.service.user.UserService;
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

import java.util.Optional;

import static com.prominent.title.constants.Constant.*;

/**
 * This controller contains apis and method related to creating new user
 **/
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * This method take username, password and communication email from the user in userLoginDto  It will check the password constraints and save user into the database.
     *
     * @param userEntryDto userEntryDto
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Saver User Data API", description = "In this save user data api, user have to enter his/her information like username,password and communication Email", tags = {"UserController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = USER_SAVED_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = UserEntryDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = ALREADY_EXISTS_CODE, description = USER_ALREADY_EXISTS),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED)
    })
    @PostMapping("/entry")
    public ResponseEntity<GenericResponse> addNewUser(@RequestBody UserEntryDto userEntryDto) {
        log.info("Creating user {}...", userEntryDto.getUserName());
        GenericResponse genericResponse = new GenericResponse(true, "User Entered Successfully", userService.addUser(userEntryDto), HttpStatus.OK.value());
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }


    /**
     * This method find all the users and returns list of users
     *
     * @param columnName columnName
     * @param size       size
     * @param page       page
     * @param order      order
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericListResponse
     */
    @Operation(summary = "Get all users User Data API", description = "This Api takes Params(columnName,size,page,order) and pagination and sorting is based on the supplied fields. " +
            "Order values can be ASC/DESC. Page starts from 1. By default if column is null then sorted by id, if order is null then sorted ascending and size is null then 10", tags = {"UserController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = FETCH_USER_LIST_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = UserListDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED)
    })
    @GetMapping("/list")
    public ResponseEntity<GenericListResponse> getAllUsersPageable(@Parameter(description = "The type of columns", schema = @Schema(allowableValues = {"userId", "firstName", "lastName", "role", "contactNumber", "address", "city", "state", "zipCode", "updateDate"})) @RequestParam(value = "columnName", defaultValue = "userId") String columnName,
                                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                                   @RequestParam(value = "page", defaultValue = "1") int page,
                                                                   @Parameter(description = "The type of orders", schema = @Schema(allowableValues = {"ASC", "DESC"}))
                                                                   @RequestParam(value = "order", defaultValue = "ASC") String order) {
        log.info("Inside get all user list.");
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAllUsers(new GenericPageableDto(columnName, size, page, order)));

    }

    /**
     * This API takes fields which is needed to be updated and update changes in the database.
     *
     * @param userDetailsDto userDetailsDto
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Update User Details API", description = "This API takes fields which need to be updated and return the updated user Details.", tags = {"UserController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = USER_UPDATED_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = EditUserDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED)
    })
    @PutMapping("/edit")
    public ResponseEntity<GenericResponse> editUser(@RequestBody UserDetailsDto userDetailsDto) {
        log.info("updating user {}...", userDetailsDto.getUserName());
        GenericResponse genericResponse = new GenericResponse(true, "User Edited Successfully", userService.editUserDetails(userDetailsDto), HttpStatus.OK.value());
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    /**
     * This API delete all records of Users from the database.
     *
     * @param emailDto emailDto
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */

    @Operation(summary = "Delete User Details API", description = "This API delete all records of Users from the database.", tags = {"UserController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = USER_DELETED_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = GenericResponse.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED)
    })
    @DeleteMapping("/delete")
    public ResponseEntity<GenericResponse> deleteUser(@RequestBody EmailDto emailDto) {

        Optional<User> optionalUser = userRepository.findByUserName(emailDto.getUserName());
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new UserNotFoundException(emailDto.getUserName());
        }

        GenericResponse genericResponse = new GenericResponse(true, USER_DELETED_SUCCESSFULLY, new EmptyJsonBody(), HttpStatus.OK.value());
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

    /**
     * This API used to get user details with username ir userId.
     *
     * @param username username
     * @param userId   userId
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Get User Details API(With username or userId)", description = "This API fetches particular record of user from the database.", tags = {"UserController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = DATA_FETCHED_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = UserDetailsDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN_REQUEST_CODE),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED)
    })
    @GetMapping("/get")
    public ResponseEntity<GenericResponse> getUser(@RequestParam(name = "username", required = false) String username, @RequestParam(name = "userId", required = false) String userId) {
        log.info("Fetching UserDetails for {} {}...", username, userId);
        GenericResponse genericResponse = new GenericResponse(true, DATA_FETCHED_SUCCESSFULLY, userService.getUserDetails(username, userId), HttpStatus.OK.value());
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }

}
