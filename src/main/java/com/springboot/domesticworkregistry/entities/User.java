package com.springboot.domesticworkregistry.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springboot.domesticworkregistry.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "birthdate")
    private LocalDate birthDate;

    @Column(name = "identificationNumber", unique = true, nullable = false)
    private String identificationNumber;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "first_login")
    private boolean firstLogin = false;

    public User(String firstName, String lastName, String email, LocalDate birthDate, String identificationNumber,
            String password,
            Role role, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.identificationNumber = identificationNumber;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }

}
