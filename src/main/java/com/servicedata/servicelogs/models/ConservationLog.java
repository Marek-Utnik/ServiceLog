package com.servicedata.servicelogs.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ConservationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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
    LocalDate publicationDate;

}