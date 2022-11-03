package com.prominent.title.entity;

public enum TransactionStep {

    STEP1("STEP1"),
    STEP2("STEP2"),
    STEP3("STEP3"),
    STEP4("STEP4"),
    STEP5("STEP5");
    public final String label;

    TransactionStep(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}