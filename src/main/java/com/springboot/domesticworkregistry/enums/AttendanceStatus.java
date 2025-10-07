package com.springboot.domesticworkregistry.enums;

public enum AttendanceStatus {
    PRESENT("PRESENTE"), JUSTIFIED_ABSCENCE("AUSENCIA JUSTIFICADA"), UNJUSTIFIED_ABSCENCE("AUSENCIA INJUSTIFICADA"),
    NATIONAL_HOLIDAY("FERIADO NACIONAL"), LATE("LLEGADA TARDE"), EXTRA_JOB("MODIFICACIÓN"), OTHER("OTRO");

    private final String label;

    AttendanceStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
