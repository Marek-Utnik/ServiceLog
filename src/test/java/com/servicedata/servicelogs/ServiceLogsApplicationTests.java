package com.servicedata.servicelogs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;

import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;

@SpringBootTest
@TestConstructor(autowireMode = ALL)
class ServiceLogsApplicationTests {

    @MockBean
    private final Loader loader;

    ServiceLogsApplicationTests(Loader loader) {
        this.loader = loader;
    }

    @Test
    void contextLoads() {
    }

}

