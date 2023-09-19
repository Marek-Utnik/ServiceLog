package com.bazakonserwacji.zeszyt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class ZeszytApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZeszytApplication.class, args);
	}

}