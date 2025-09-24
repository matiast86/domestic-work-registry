package com.springboot.domesticworkregistry.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "home_address_id")
    private Address homeAddress;

    @OneToMany(mappedBy = "employee", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH }, fetch = FetchType.LAZY)
    private List<Contract> contracts = new ArrayList<>();

    public void addContract(Contract contract) {

        contracts.add(contract);
        contract.setEmployee(this);

    }

}
