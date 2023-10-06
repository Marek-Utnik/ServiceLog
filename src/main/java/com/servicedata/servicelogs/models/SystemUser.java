package com.servicedata.servicelogs.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.servicedata.servicelogs.validators.UniqueUsername;
import com.servicedata.servicelogs.validators.ValidPasswords;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;

@UniqueUsername
@ValidPasswords
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "person")
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long systemUserId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Transient
    private String repeatedPassword;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @ManyToMany(cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "systemuser_companies",
            joinColumns = @JoinColumn(name = "system_user_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    private Set<Company> companies;

    @ManyToMany(cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "systemuser_authorities",
            joinColumns = @JoinColumn(name = "system_user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    private Set<Authority> authorities;

    @OneToMany(mappedBy = "systemUser")
    @JsonIgnoreProperties("systemUser")
    private Set<ConservationLog> conservationLogs;

    public SystemUser(String username, String password, String name, String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

}