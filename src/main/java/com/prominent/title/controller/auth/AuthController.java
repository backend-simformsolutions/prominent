package com.prominent.title.controller.auth;

import com.prominent.title.dto.EmailDto;
import com.prominent.title.dto.ResetPasswordDto;
import com.prominent.title.dto.response.EmptyJsonBody;
import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.dto.user.UserLoginDto;
import com.prominent.title.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

import static com.prominent.title.constants.Constant.*;

/**
 * This controller contains apis and method related to log in, forgot password, verify token and reset password
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * This API take username, password from the user in userLoginDto and verifies from the database.
     *
     * @param userLoginDto userLoginDto
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */

    @Operation(summary = "Login User API", description = "Here ,user have to enter username and password also need to enter communicationEmail for forgot password & any type of communication needed in future", tags = {"Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = USER_LOGGED_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = UserLoginDto.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = NOT_FOUND_CODE, description = USER_NOT_FOUND),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN)
    })
    @PostMapping("/login")
    public ResponseEntity<GenericResponse> userLogin(@RequestBody UserLoginDto userLoginDto) {
        log.info("User {} logging in...", userLoginDto.getUserName());
        GenericResponse genericResponse = authService.verifyCredentials(userLoginDto);
        return new ResponseEntity<>(genericResponse, HttpStatus.valueOf(genericResponse.getCode()));
    }

    /**
     * This API takes the communication email from Email dto and generates the token which will be used for resetting the password
     *
     * @param emailDto emailDto
     * @return {@link ResponseEntity}
     * @throws MessagingException javax.mail. messaging exception
     * @throws IOException        java.io. i o exception
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Forgot Password API", description = "In the forgot password user have to enter communicationEmail where it receive the mail from admin and then user can reset the password", tags = {"Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = LINK_SENT_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = GenericResponse.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN)
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<GenericResponse> forgotPassword(@RequestBody EmailDto emailDto) throws MessagingException, IOException {
        log.info("Inside forgot password for {}...", emailDto.getUserName().toLowerCase());
        GenericResponse genericResponse = authService.createPasswordResetTokenForUser(emailDto.getUserName().toLowerCase());
        return new ResponseEntity<>(genericResponse, HttpStatus.valueOf(genericResponse.getCode()));
    }

    /**
     * This API takes new password from user and reset the password if password constraints are satisfied.
     *
     * @param resetPasswordDto   resetPasswordDto
     * @param resetPasswordToken resetPasswordToken
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */
    @Operation(summary = "Reset Password API", description = "In this reset password API, user can change his/her password by entering new password and confirm password.", tags = {"Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = PASSWORD_CHANGED_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = GenericResponse.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN)
    })
    @PostMapping("/reset-password/{resetPasswordToken}")
    public ResponseEntity<GenericResponse> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto, @PathVariable("resetPasswordToken") String resetPasswordToken) {

        log.info("Inside reset Password...");
        boolean isPasswordChanged = authService.resetPassword(resetPasswordDto, resetPasswordToken);
        GenericResponse genericResponse = new GenericResponse(isPasswordChanged, ((isPasswordChanged) ? "Password Changed Successfully" : "Invalid Token"), new EmptyJsonBody(), isPasswordChanged ? 200 : 400);
        return new ResponseEntity<>(genericResponse, HttpStatus.valueOf(genericResponse.getCode()));
    }

    /**
     * This API logouts the user.
     *
     * @param request request
     * @return {@link ResponseEntity}
     * @see ResponseEntity
     * @see GenericResponse
     */

    @Operation(summary = "Logout API", description = "In the logout API user will be logged out and token will be expired.", tags = {"Auth"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SUCCESS_CODE, description = LINK_SENT_SUCCESSFULLY, content = @Content(schema = @Schema(implementation = GenericResponse.class))),
            @ApiResponse(responseCode = BAD_REQUEST_CODE, description = BAD_REQUEST),
            @ApiResponse(responseCode = FORBIDDEN_REQUEST_CODE, description = FORBIDDEN)
    })
    @PostMapping("/logout")
    public ResponseEntity<GenericResponse> logout(HttpServletRequest request) {
        authService.logout(request);
        return new ResponseEntity<>(new GenericResponse(true, LOGOUT_SUCCESSFULLY, new EmptyJsonBody(), HttpStatus.OK.value()), HttpStatus.OK);
    }
}
