package com.bazakonserwacji.zeszyt.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

@Configuration
public class ThymeleafAutoConfiguration {
    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }
}
