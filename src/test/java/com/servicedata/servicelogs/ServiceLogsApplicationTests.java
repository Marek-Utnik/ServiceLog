package com.servicedata.servicelogs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import com.servicedata.servicelogs.controllers.CompanyController;
import com.servicedata.servicelogs.services.EmailService;

import lombok.RequiredArgsConstructor;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
@RequiredArgsConstructor
class ServiceLogsApplicationTests {

    @MockBean
    Loader loader;

    @MockBean
    EmailService emailService;
    
    @MockBean
    CompanyController companyController;

    @Test
    void contextLoads() {
    }

}

