package com.servicedata.servicelogs.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company{
	
	@Id
	@GeneratedValue
	private Long companyId;
	
	private String companyName;
	
	private String companyAddress;
	
	@OneToMany(mappedBy = "company")
	@JsonIgnoreProperties("company")
	private Set<Machine> machines;
	
    @ManyToMany(mappedBy = "companies")
    private Set<SystemUser> systemUsers;
	
	public Company(String companyName, 
			String companyAddress) {
		this.companyName = companyName;
		this.companyAddress = companyAddress;
	}
		
}