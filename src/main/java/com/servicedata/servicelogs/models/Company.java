package com.servicedata.servicelogs.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue
    private Long companyId;

    private String companyName;

    private String companyAddress;

    @OneToMany(mappedBy = "company")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<Machine> machines;

    @ManyToMany(mappedBy = "companies")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SystemUser> systemUsers;

    public Company(String companyName,
                   String companyAddress) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

}