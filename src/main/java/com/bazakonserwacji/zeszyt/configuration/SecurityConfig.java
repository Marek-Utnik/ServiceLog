package com.bazakonserwacji.zeszyt.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.bazakonserwacji.zeszyt.services.SystemUserDetailsService;
import com.bazakonserwacji.zeszyt.services.SystemUserService;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;



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