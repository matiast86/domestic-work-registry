package com.springboot.domesticworkregistry.enums;

public enum DayOfTheWeek {
    MONDAY("LUNES"), TUESDAY("MARTES"), WEDNESDAY("MIERCOLES"), THURSDAY("JUEVES"), FRIDAY("VIERNES"),
    SATURDAY("SABADO"), SUNDAY("DOMINGO");

    private final String label;

    DayOfTheWeek(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
