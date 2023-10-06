package com.servicedata.servicelogs.services;

import com.servicedata.servicelogs.exceptions.MachineNotFoundException;
import com.servicedata.servicelogs.forms.MachineFilterData;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.repositories.MachineRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor

public class MachineService {
    private final MachineRepository machineRepository;

    public Machine findMachineById(long machineId) {
        return machineRepository.findById(machineId)
                .orElseThrow(() -> new MachineNotFoundException("No machine found with the following ID: %d".formatted(machineId)));
    }

    public Page<Machine> filteredMachine(Pageable pageable,
                                         Company company,
                                         MachineFilterData filterData
    ) {
        boolean machineIdCheck = filterData.getMachineId() != null;
        boolean registrationNumberCheck = filterData.getRegistrationNumber() != null;
        boolean serialNumberCheck = filterData.getSerialNumber() != null;
        boolean producerNameCheck = filterData.getProducerName() != null && !filterData.getProducerName().isBlank();
        boolean machineTypeCheck = filterData.getMachineType() != null && !filterData.getMachineType().isBlank();

        Specification<Machine> specification = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("company"), company));

        if (machineIdCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("machineId"), filterData.getMachineId()));
        }
        if (registrationNumberCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("registrationNumber"), filterData.getRegistrationNumber()));
        }
        if (serialNumberCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("serialNumber"), filterData.getSerialNumber()));
        }
        if (producerNameCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("producerName")), "%" + filterData.getProducerName().toUpperCase() + "%"));
        }
        if (machineTypeCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("machineType")), "%" + filterData.getMachineType().toUpperCase() + "%"));
        }
        
        Page<Machine> page = machineRepository.findAll(specification, pageable);    
        return page;
    }

}