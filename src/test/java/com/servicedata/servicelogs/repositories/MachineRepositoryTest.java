package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.controllers.CompanyController;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.services.EmailService;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
class MachineRepositoryTest {

    @MockBean
    Loader loader;
    
    @MockBean
    EmailService emailService;
    
    @MockBean
    CompanyController companyController;

    private final MachineRepository machineRepository;

    @Test
    void findByCompanyNamePageableReturnsCorrectMachinePage() {
        // given
        String companyName = "Pekabex";
        Pageable pageable = PageRequest.of(0, 20, Sort.by("machineId"));
        // when
        Page<Machine> machinePage = machineRepository.findByCompanyNamePageable(pageable, companyName);

        // then
        assertNotNull(machinePage);
        assertEquals(companyName, machinePage.getContent().get(0).getCompany().getCompanyName());
    }

}