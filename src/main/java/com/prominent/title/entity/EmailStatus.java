package com.prominent.title.entity;

public enum EmailStatus {
    PENDING("PENDING"),
    SENT("SENT"),
    ERROR("ERROR");

    public final String label;

    EmailStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
