package com.prominent.title.controller.signup;

import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.dto.user.UserSignupDto;
import com.prominent.title.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.prominent.title.constants.Constant.*;


@RestController
@RequestMapping("/signup")
@Slf4j
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This method take username, password and phone number  It will check the password with confirm password and save seller into the database.
     *
     * @param userSignupDto userSignupDto
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "User Signup API", description = "In this user signup API, user have to enter his/her information like username,password and phone number", tags = {"SignupController"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = USER_SAVED_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = UserSignupDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN),
            @ApiResponse(responseCode = UNAUTHORIZED_CODE, description = UNAUTHORIZED)
    })
    @PostMapping("/user")
    public ResponseEntity<GenericResponse> userSignup(@Valid @RequestBody UserSignupDto userSignupDto) {
        log.info("Inside Seller user {}...", userSignupDto.getUserName());
        GenericResponse genericResponse = new GenericResponse(true, "User saved Successfully", userService.addUser(userSignupDto, "General User"), HttpStatus.OK.value());
        return new ResponseEntity<>(genericResponse, HttpStatus.OK);
    }
}
