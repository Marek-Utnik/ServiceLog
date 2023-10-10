package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.controllers.CompanyController;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.services.EmailService;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
class ConservationLogRepositoryTest {

    @MockBean
    Loader loader;
    
    @MockBean
    EmailService emailService;
    
    @MockBean
    CompanyController companyController;

    private final ConservationLogRepository conservationLogRepository;
    private final MachineRepository machineRepository;


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