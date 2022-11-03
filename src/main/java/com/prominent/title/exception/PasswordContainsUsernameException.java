package com.prominent.title.exception;

import javax.validation.ValidationException;

public class PasswordContainsUsernameException extends ValidationException {
    public PasswordContainsUsernameException(String message) {
        super(message);
    }
}
