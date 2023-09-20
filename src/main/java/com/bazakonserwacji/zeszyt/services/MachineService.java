package com.bazakonserwacji.zeszyt.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bazakonserwacji.zeszyt.exceptions.MachineNotFoundException;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.repositories.MachineRepository;

@Service
@Transactional
public class MachineService {
    private final MachineRepository machineRepository;

    public MachineService(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
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
}