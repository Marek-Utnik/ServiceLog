package com.servicedata.servicelogs.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.servicedata.servicelogs.services.SystemUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
    private final SystemUserDetailsService systemUserDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public SecurityConfig(SystemUserDetailsService systemUserDetailsService) {
        this.systemUserDetailsService = systemUserDetailsService;
    }
    
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){ 
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); 
    	provider.setPasswordEncoder(bCryptPasswordEncoder()); 
    	provider.setUserDetailsService(systemUserDetailsService);
    return provider; }
	
	
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {

		httpSecurity.authorizeHttpRequests(
	            		auth -> auth.requestMatchers("/error","/contact","/css/**", "/fragments/**").permitAll()
	            		.requestMatchers("/register","/new-user").anonymous()
	                    .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
	                    .requestMatchers("/company/**").hasAnyAuthority("ROLE_ADMIN","ROLE_COMPANYUSER")
	                    .requestMatchers("/service/**").hasAnyAuthority("ROLE_ADMIN","ROLE_SERVICEMAN")
	                    .anyRequest().authenticated()
					)
					.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.defaultSuccessUrl("/", true)
						.permitAll()
					);


		return	httpSecurity.build();
	}
}