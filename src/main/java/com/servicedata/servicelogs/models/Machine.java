package com.servicedata.servicelogs.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long machineId;


    private int registrationNumber;

    private int serialNumber;

    @NotBlank(message = "Producer Name is required")
    private String producerName;

    @NotBlank
    private String machineType;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties("machines")
    private Company company;

    @OneToMany(mappedBy = "machine")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnoreProperties("machine")
    private Set<ConservationLog> conservationLogs;

}