package com.prominent.title.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.prominent.title.dto.response.EmptyJsonBody;
import com.prominent.title.dto.response.GenericResponse;
import com.prominent.title.exception.*;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global Exception Handler for handling the exceptions which occurs during the runtime.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> exception(BadCredentialsException exception) {
        GenericResponse genericResponse = new GenericResponse(false, exception.getMessage(), new EmptyJsonBody(), HttpStatus.FORBIDDEN.value());
        log.error("Error occurred due to bad credentials.");
        return new ResponseEntity<>(genericResponse, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> exception(ConstraintViolationException exception) {
        GenericResponse genericResponse = new GenericResponse(false, exception.getMessage(), new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value());
        log.error("Error occurred due to constrain violation.");
        return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> exception(MethodArgumentNotValidException exception) {
        Map<String, Object> response = new HashMap<>();

        if (exception.hasFieldErrors()) {
            List<Map<String, String>> errors = new ArrayList<>();

            for (FieldError error : exception.getFieldErrors()) {
                Map<String, String> transformedError = new HashMap<>();
                transformedError.put("field", error.getField());
                transformedError.put("error", error.getDefaultMessage());

                errors.add(transformedError);
            }
            response.put("errors", errors);
        }

        GenericResponse genericResponse = new GenericResponse(false, "Validation Failed", response, HttpStatus.BAD_REQUEST.value());
        log.error("Error occurred due to method arguments are not valid.");
        return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PasswordContainsUsernameException.class)
    public ResponseEntity<Object> exception(PasswordContainsUsernameException exception) {
        GenericResponse genericResponse = new GenericResponse(false, exception.getMessage(), null, HttpStatus.BAD_REQUEST.value());
        log.error("Error occurred due to password contains username.");
        return new ResponseEntity<>(genericResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<Object> exception(DataIntegrityViolationException exception) {
        log.error("Error occurred due to data integrity violation exception.");
        return new ResponseEntity<>(new GenericResponse(false, exception.getMessage() + " Already Exists", new EmptyJsonBody(), HttpStatus.CONFLICT.value()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> exception(UserNotFoundException exception) {
        log.error("Error occurred due to user not found");
        return new ResponseEntity<>(new GenericResponse(false, "Cannot Find User With This Information " + exception.getMessage(), new EmptyJsonBody(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public ResponseEntity<Object> exception(ExpiredJwtException exception) {
        log.error("Error occurred due to JWT token expired.");
        return new ResponseEntity<>(new GenericResponse(false, "Token Expired. Generate New Token", new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<Object> exception(UsernameNotFoundException exception) {
        log.error("Error occurred due to user name not found.");
        return new ResponseEntity<>(new GenericResponse(false, exception.getMessage(), new EmptyJsonBody(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = PaymentInformationNotFoundException.class)
    public ResponseEntity<Object> exception(PaymentInformationNotFoundException exception) {
        log.error("Error occurred while payment information not found.");
        return new ResponseEntity<>(new GenericResponse(false, exception.getMessage(), new EmptyJsonBody(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MessagingException.class)
    public ResponseEntity<Object> exception(MessagingException exception) {
        log.error("Error occurred due to messaging exception.");
        return new ResponseEntity<>(new GenericResponse(false, "Could not send Email, Please try again", new EmptyJsonBody(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = UnsupportedEncodingException.class)
    public ResponseEntity<Object> exception(UnsupportedEncodingException exception) {
        log.error("Error occurred due to unsupported encoding exception.");
        return new ResponseEntity<>(new GenericResponse(false, "Could not send Email, Please try again", new EmptyJsonBody(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = OrganizationNotFoundException.class)
    public ResponseEntity<Object> exception(OrganizationNotFoundException exception) {
        log.error("Error occurred due to organization not found.");
        return new ResponseEntity<>(new GenericResponse(false, "Cannot Find Organization With This Name Or Id " + exception.getMessage(), new EmptyJsonBody(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ProfilePasswordException.class)
    public ResponseEntity<Object> exception(ProfilePasswordException exception) {
        log.error("Error occurred due to profile password exception.");
        return new ResponseEntity<>(new GenericResponse(false, exception.getMessage(), new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<Object> exception(RoleNotFoundException exception) {
        log.error("Error occurred while role not found.");
        return new ResponseEntity<>(new GenericResponse(false, exception.getMessage(), new EmptyJsonBody(), HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> exception(IllegalArgumentException exception) {
        log.error("Error occurred due to Illegal argument exception.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(false, exception.getMessage(), new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = InvalidColumnNameException.class)
    public ResponseEntity<Object> exception(InvalidColumnNameException exception) {
        log.error("Error occurred due to invalid column/property name : " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(false, "Invalid Column Name : " + exception.getMessage(), new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = InvalidFormatException.class)
    public ResponseEntity<Object> exception(InvalidFormatException exception) {
        log.error("Error occurred due to invalid format exception.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(false, "Invalid value: " + exception.getValue(), new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = AddressNotFoundException.class)
    public ResponseEntity<Object> exception(AddressNotFoundException exception) {
        log.error("Error occurred while Address not found.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(false, "Address Not Found for User/Organization/Transaction Id: " + exception.getMessage(), new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = CustomEncryptionDecryptionException.class)
    public ResponseEntity<Object> exception(CustomEncryptionDecryptionException exception) {
        log.error("Error occurred while encrypting/decrypting");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(false, exception.getMessage(), new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(value = CityAlreadyExistsException.class)
    public ResponseEntity<Object> exception(CityAlreadyExistsException exception) {
        log.error("Error occurred due to city already exists.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericResponse(false, "County/City Already Exists " + exception.getMessage(), new EmptyJsonBody(), HttpStatus.BAD_REQUEST.value()));
    }
}
