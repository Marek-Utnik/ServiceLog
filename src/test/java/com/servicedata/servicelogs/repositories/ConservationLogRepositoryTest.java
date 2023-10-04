package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class ConservationLogRepositoryTest {

    @MockBean
    private final Loader loader;

    private final ConservationLogRepository conservationLogRepository;
    private final MachineRepository machineRepository;

    ConservationLogRepositoryTest(ConservationLogRepository conservationLogRepository,
                                  MachineRepository machineRepository, Loader loader) {
        this.conservationLogRepository = conservationLogRepository;
        this.machineRepository = machineRepository;
        this.loader = loader;
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
        assertNotNull(conservationLogs);
        assertEquals(machineId, conservationLog.getMachine().getMachineId());

    }

}