package com.springboot.domesticworkregistry.enums;

public enum PayslipStatus {
    DRAFT("BORRADOR"), APPROVED("APROBADO"), PAID("PAGADO");

    private final String label;

    PayslipStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
