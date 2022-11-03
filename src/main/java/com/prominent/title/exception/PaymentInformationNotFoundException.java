package com.prominent.title.exception;

public class PaymentInformationNotFoundException extends RuntimeException {
    public PaymentInformationNotFoundException(String message) {
        super(message);
    }
}
