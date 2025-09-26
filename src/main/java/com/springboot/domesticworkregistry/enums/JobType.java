package com.springboot.domesticworkregistry.enums;

public enum JobType {
    NANNY("CUIDADO DE PERSONAS"),
    HOUSE_KEEPER("TAREAS GENERALES");

    private final String label;

    JobType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

