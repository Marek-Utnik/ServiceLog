package com.servicedata.servicelogs.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;


import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.*;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class ConservationLogRepositoryTest {

    private final ConservationLogRepository conservationLogRepository;
    private final MachineRepository machineRepository;

    ConservationLogRepositoryTest(ConservationLogRepository conservationLogRepository,
    								MachineRepository machineRepository) {
        this.conservationLogRepository = conservationLogRepository;
        this.machineRepository = machineRepository;
    }

    @Test
    void findByMachineReturnsCorrectConservationLogList() {
        // given
    	Long machineId = 402L;
    	Machine machine = machineRepository.findById(machineId).get();
    	// when
    	List<ConservationLog> conservationLogs = conservationLogRepository.findByMachine(machine);
    	ConservationLog conservationLog = conservationLogs.get(0);
       	// then
        assertTrue(conservationLogs!=null);
        assertEquals(machineId, conservationLog.getMachine().getMachineId());
        
    }

}