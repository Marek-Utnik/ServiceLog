package com.bazakonserwacji.zeszyt.models;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Machine{
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	@GenericGenerator(name = "native",strategy = "native")
	private Long machineId;
	
	private int registrationNumber;
	private int serialNumber;
	private String producerName;
	private String machineType;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	@JsonIgnoreProperties("machines")
	private Company company;
	
	@OneToMany(mappedBy = "machine")
	@JsonIgnoreProperties("machine")
	private Set<ConservationLog> conservationLogs;
		
}