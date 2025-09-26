package com.springboot.domesticworkregistry.enums;

public enum EmploymentType {
    HOURLY("POR HORA"),
    MONTHLY("MENSUAL");

    private final String label;

    EmploymentType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
