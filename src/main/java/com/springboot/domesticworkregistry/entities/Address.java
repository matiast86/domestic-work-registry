package com.springboot.domesticworkregistry.entities;

public class Address {
    private int id;
    private String number;
    private String city;
    private String postalCode;
    private String country;

    public Address() {
    }

    public Address(String number, String city, String postalCode, String country) {
        this.number = number;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address {id=" + id + ", number=" + number + ", city=" + city + ", postalCode=" + postalCode
                + ", country=" + country + "}";
    }

    
}
