package com.servicedata.servicelogs.services;

import com.servicedata.servicelogs.exceptions.MachineNotFoundException;
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

    public void addNewMachine(Machine machine) {
        machineRepository.save(machine);
    }

    public void deleteMachineById(long machineId) {
        machineRepository.deleteById(machineId);
    }

    public void updateMachine(Machine machine) {
        machineRepository.save(machine);
    }

    public Machine findMachineById(long machineId) {
        return machineRepository.findById(machineId)
                .orElseThrow(() -> new MachineNotFoundException("No machine found with the following ID: %d".formatted(machineId)));
    }

    public Page<Machine> listMachines(Pageable pageable) {
        return machineRepository.findAll(pageable);
    }

    public Page<Machine> filteredMachine(Pageable pageable,
                                         Company company,
                                         Long machineId,
                                         Integer registrationNumber,
                                         Integer serialNumber,
                                         String producerName,
                                         String machineType
    ) {
        boolean machineIdCheck = machineId != null;
        boolean registrationNumberCheck = registrationNumber != null;
        boolean serialNumberCheck = serialNumber != null;
        boolean producerNameCheck = producerName != null && !producerName.isBlank();
        boolean machineTypeCheck = machineType != null && !machineType.isBlank();

        Specification<Machine> specification = Specification.where(null);

        if (machineIdCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("machineId"), machineId));
        }
        if (registrationNumberCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("registrationNumber"), registrationNumber));
        }
        if (serialNumberCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("serialNumber"), serialNumber));
        }
        if (producerNameCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("producerName")), "%" + producerName.toUpperCase() + "%"));
        }
        if (machineTypeCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("machineType")), "%" + machineType.toUpperCase() + "%"));
        }
        
        Page<Machine> page = machineRepository.findAll(specification, pageable);    
        return page;
    }

}