package com.springboot.domesticworkregistry.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springboot.domesticworkregistry.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = { "roles", "employerContracts", "employeeContracts", "address" })
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserRole> roles = new HashSet<>();

    @Column(name = "phone")
    private String phone;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "employer", fetch = FetchType.LAZY)
    private List<Contract> employerContracts = new ArrayList<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<Contract> employeeContracts = new ArrayList<>();

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(name = "reset_token_expiry")
    private LocalDateTime resetTokenExpiry;

    public User(String firstName, String lastName, String email, LocalDate birthDate,
            String identificationNumber, String password, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.identificationNumber = identificationNumber;
        this.password = password;
        this.phone = phone;
    }

    // === Utility Methods ===

    public boolean hasRole(Role role) {
        return roles.stream().anyMatch(userRole -> userRole.getRole() == role);
    }

    public void addRole(Role role) {
        if (!hasRole(role)) {
            UserRole userRole = new UserRole();
            userRole.setRole(role);
            userRole.setUser(this);
            roles.add(userRole);
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    // === Spring Security ===

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().name()))
                .toList();
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

    // === Contract helpers ===
    public void addEmployerContract(Contract contract) {
        employerContracts.add(contract);
        contract.setEmployer(this);
    }

    public void addEmployeeContract(Contract contract) {
        employeeContracts.add(contract);
        contract.setEmployee(this);
    }

    public boolean isResetTokenValid() {
        return resetToken != null && resetTokenExpiry != null && resetTokenExpiry.isAfter(LocalDateTime.now());
    }
}
