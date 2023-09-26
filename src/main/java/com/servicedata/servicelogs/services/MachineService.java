package com.servicedata.servicelogs.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicedata.servicelogs.exceptions.MachineNotFoundException;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.repositories.MachineRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional
public class MachineService {
    private final MachineRepository machineRepository;

    //Obiekt zarządzający Enitities
    private final EntityManager entityManager;

    public MachineService(MachineRepository machineRepository, EntityManager entityManager) {
        this.machineRepository = machineRepository;
        this.entityManager = entityManager;
    }

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
    		){
        boolean machineIdCheck = machineId != null;
        boolean registrationNumberCheck = registrationNumber != null;
        boolean serialNumberCheck = serialNumber != null;
        boolean producerNameCheck = producerName != null && !producerName.isBlank();
        boolean machineTypeCheck = machineType != null && !machineType.isBlank();

        //Obiekt budujący zapytanie do bazy
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //Obiekt zapytania do bazy - jakie będą zwracane obiekty
        CriteriaQuery<Machine> criteriaQuery = criteriaBuilder.createQuery(Machine.class);
        //Gdzie będziemy szukać
        Root<Machine> machineRoot = criteriaQuery.from(Machine.class);
        //Określamy że to będzie select
        CriteriaQuery<Machine> select = criteriaQuery.select(machineRoot);
        //Pusta lista warunków
        List<Predicate> predicates = new ArrayList<>();
        
        
        predicates.add(criteriaBuilder.equal(machineRoot.get("company"), company));
        if (machineIdCheck) {
            predicates.add(criteriaBuilder.equal(machineRoot.get("machineId"), machineId));
        }
        if (registrationNumberCheck) {
            predicates.add(criteriaBuilder.equal(machineRoot.get("registrationNumber"), registrationNumber));
        }
        if (serialNumberCheck) {
            predicates.add(criteriaBuilder.equal(machineRoot.get("serialNumber"), serialNumber));
        }
        if (producerNameCheck) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(machineRoot.get("producerName")), "%" + producerName.toUpperCase() + "%"));
        }
        if (machineTypeCheck) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(machineRoot.get("machineType")), "%" + machineType.toUpperCase() + "%"));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), machineRoot, criteriaBuilder));

        List <Machine> filteredMachines = entityManager.createQuery(criteriaQuery).getResultList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredMachines.size());
        List<Machine> pageContent = filteredMachines.subList(start, end);        
        Page <Machine> page = new PageImpl(pageContent, pageable, filteredMachines.size());
        return page;
    }
    
}