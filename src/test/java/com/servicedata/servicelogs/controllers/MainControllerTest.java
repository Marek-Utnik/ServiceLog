package com.servicedata.servicelogs.controllers;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.context.TestConstructor.AutowireMode.ALL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.servicedata.servicelogs.services.SystemUserService;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
	
	@Autowired
	protected MockMvc mockMvc;
	
	@Test
	public void testContactPage() throws Exception {
		mockMvc.perform(get("/contact"))
		.andExpect(status().isOk())
		.andExpect(view().name("contact"))
		.andExpect(content().string(
				containsString("Zeszyt konserwacji")));
		}
	}