package com.domesticworkregistry.backend.entities;

import java.util.ArrayList;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;
import org.springframework.stereotype.Component;

@Component
public class User {
    private int id;
    private String name;
    private String surname;
    private int age;
    private ArrayList<Address> addresses;
    private String birthdate;
    private String identificationNumber;

    public User() {
    }

    public User(String name, String surname, int age, ArrayList<Address> addresses, String birthdate,
            String identificationNumber) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.addresses = addresses;
        this.birthdate = birthdate;
        this.identificationNumber = identificationNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", surname=" + surname + ", age=" + age + ", addresses="
                + addresses + ", birthdate=" + birthdate + ", identificationNumber=" + identificationNumber + "]";
    }

}
