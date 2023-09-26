package com.servicedata.servicelogs.models;

import java.util.Date;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConservationLog{
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO,generator="native")
	@GenericGenerator(name = "native",strategy = "native")
	private Long conservationLogId;
	
	
	@ManyToOne
	@JoinColumn(name = "system_user_id", nullable = false)
	@JsonIgnoreProperties("conservation_logs")
	private SystemUser systemUser;
	
	
	@ManyToOne
	@JoinColumn(name = "machine_id", nullable = false)
	@JsonIgnoreProperties("conservation_logs")
	private Machine machine;
	
	@Column(nullable = false)
	private String conservationDescription;
	
    @Temporal(TemporalType.DATE)
    Date publicationDate;
	
}