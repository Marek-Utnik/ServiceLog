package com.servicedata.servicelogs.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.servicedata.servicelogs.controllers.MachineController;
import com.servicedata.servicelogs.excelgenerators.CompanyExcelGenerator;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Slf4j
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
    @JsonIgnoreProperties("company")
    private Set<Machine> machines;

    @ManyToMany(mappedBy = "companies")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SystemUser> systemUsers;

    public Company(String companyName,
                   String companyAddress) {
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }
    
    public void generateExcel(HttpServletResponse response,     
    						  LocalDate publicationDateStart,
    						  LocalDate publicationDateEnd) {    	
        SortedMap<Machine, List<ConservationLog>> logs = new TreeMap<>();
        List<Machine> machines = new ArrayList<> (this.getMachines());
        machines.sort(Comparator.comparing(Machine::getMachineId));
        for (Machine machine : machines) {
        	List <ConservationLog> log = machine.getConservationLogs().stream()
        			.filter(p -> p.getPublicationDate().isAfter(publicationDateStart))
        			.filter(p -> p.getPublicationDate().isBefore(publicationDateEnd))
        			.collect(Collectors.toList());
        	logs.put(machine, log);
        }

    	CompanyExcelGenerator file = new CompanyExcelGenerator(this, logs);
    	
    	try {
    		file.generateExcelFile(response);
    	}
    	catch(IOException e){
            log.error("Error: ", e);
    	}
    	
    }


}