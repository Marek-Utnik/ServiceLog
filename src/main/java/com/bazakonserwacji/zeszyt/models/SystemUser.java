package com.bazakonserwacji.zeszyt.models;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.bazakonserwacji.zeszyt.validators.UniqueUsername;
import com.bazakonserwacji.zeszyt.validators.ValidPasswords;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@UniqueUsername
@ValidPasswords
@Entity
@NoArgsConstructor
@Getter
@Setter
public class SystemUser{

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	@GenericGenerator(name = "native",strategy = "native")
	private Long systemUserId;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Transient
	String repeatedPassword;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String surname;
	
	@ManyToMany(cascade=CascadeType.ALL) 
	@JoinTable(name = "systemuser_companies",
			   joinColumns = @JoinColumn(name = "system_user_id"),
			   inverseJoinColumns = @JoinColumn(name = "company_id"))
	Set<Company> companies;
	
	@ManyToMany(cascade=CascadeType.MERGE)
	@JoinTable(name = "systemuser_authorities",
			   joinColumns = @JoinColumn(name = "system_user_id"),
			   inverseJoinColumns = @JoinColumn(name = "authority_id"))
	Set<Authority> authorities;
	

	@OneToMany(mappedBy = "systemUser")
	@JsonIgnoreProperties("systemUser")
	private Set<ConservationLog> conservationLogs;
	
    public SystemUser(String username, String password, String name,  String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }
    
    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
	
}