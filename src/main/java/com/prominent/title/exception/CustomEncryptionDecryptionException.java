package com.prominent.title.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomEncryptionDecryptionException extends RuntimeException {
    private final String message;
}
