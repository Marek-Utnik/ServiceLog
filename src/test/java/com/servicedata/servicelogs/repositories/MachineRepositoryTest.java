package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.Loader;
import com.servicedata.servicelogs.models.Machine;
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
class MachineRepositoryTest {

    @MockBean
    private final Loader loader;

    private final MachineRepository machineRepository;

    MachineRepositoryTest(MachineRepository machineRepository, Loader loader) {
        this.machineRepository = machineRepository;
        this.loader = loader;
    }

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