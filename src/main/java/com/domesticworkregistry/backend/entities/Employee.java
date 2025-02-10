package com.domesticworkregistry.backend.entities;

import org.springframework.stereotype.Component;

import com.domesticworkregistry.backend.enums.Role;

@Component
public class Employee {

    private int id;
    private String cuil;
    private Role role;

    public Employee() {
    }

    public Employee(String cuil, Role role) {
        this.cuil = cuil;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCuil() {
        return cuil;
    }

    public void setCuil(String cuil) {
        this.cuil = cuil;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", cuil=" + cuil + ", role=" + role + "]";
    }

}
