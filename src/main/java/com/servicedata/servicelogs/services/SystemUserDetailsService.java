package com.servicedata.servicelogs.services;

import com.servicedata.servicelogs.models.Authority;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.SystemUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SystemUserDetailsService implements UserDetailsService {

    private final SystemUserRepository systemUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser systemUser = systemUserRepository.findByUsername(username);

        if (systemUser == null) {
            throw new UsernameNotFoundException(username);
        }

        return buildUserDetails(systemUser);
    }


    private UserDetails buildUserDetails(SystemUser systemUser) {
        List<GrantedAuthority> authorities = getUserAuthorities(systemUser);
        return new User(systemUser.getUsername(), systemUser.getPassword(), authorities);
    }

    private List<GrantedAuthority> getUserAuthorities(SystemUser systemUser) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Authority> authorities = systemUser.getAuthorities();
        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName().toString()));
        }

        for (Company company : systemUser.getCompanies()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(company.getCompanyName().toString()));
        }
        return new ArrayList<>(grantedAuthorities);
    }
}