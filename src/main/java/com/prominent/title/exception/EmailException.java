package com.prominent.title.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailException extends RuntimeException {
    private final String message;
}
