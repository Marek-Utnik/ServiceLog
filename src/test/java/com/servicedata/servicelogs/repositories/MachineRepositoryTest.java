package com.servicedata.servicelogs.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;


import com.servicedata.servicelogs.models.Machine;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.TestConstructor.AutowireMode.*;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class MachineRepositoryTest {

    private final MachineRepository machineRepository;

    MachineRepositoryTest(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }
    
    @Test
    void findByCompanyNamePageableReturnsCorrectMachinePage() {
        // given
    	String companyName = "Pekabex";
    	Pageable pageable = PageRequest.of(0, 20, Sort.by("machineId"));
    	// when
    	Page <Machine> machinePage = machineRepository.findByCompanyNamePageable(pageable, companyName);

       	// then
        assertTrue(machinePage!=null);
        assertEquals(companyName, machinePage.getContent().get(0).getCompany().getCompanyName());
    }

}