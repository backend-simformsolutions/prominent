package com.prominent.title.entity;

public enum PaymentType {

    CHEQUE("CHEQUE"),
    CASH("CASH"),
    FEDEX("FEDEX"),
    WIRE("WIRE");

    public final String label;

    PaymentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}

