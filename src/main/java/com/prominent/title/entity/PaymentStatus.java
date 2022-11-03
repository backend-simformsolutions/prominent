package com.prominent.title.entity;

public enum PaymentStatus {

    PENDING("PENDING"),
    RECEIVED("RECEIVED");

    public final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}

