package com.bazakonserwacji.zeszyt.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bazakonserwacji.zeszyt.exceptions.MachineNotFoundException;
import com.bazakonserwacji.zeszyt.exceptions.SystemUserNotFoundException;
import com.bazakonserwacji.zeszyt.models.Authority;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.repositories.AuthorityRepository;
import com.bazakonserwacji.zeszyt.repositories.SystemUserRepository;

import java.util.HashSet;
import java.util.List;

@Service
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${my.admin.username}")
    private String myAdminUsername;

    @Value("${my.admin.password}")
    private String myAdminPassword;

    public SystemUserService(SystemUserRepository systemUserRepository, AuthorityRepository authorityRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.systemUserRepository = systemUserRepository;
        this.authorityRepository = authorityRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public void saveSystemUser(SystemUser systemUser) {
        String hashedPassword = bCryptPasswordEncoder.encode(systemUser.getPassword());
        systemUser.setPassword(hashedPassword);
    	systemUserRepository.save(systemUser);
    }
    
    public void prepareAdmin() {
        if (systemUserRepository.findByUsername(myAdminUsername) != null) {
            return;
        }

        String hashedPassword = bCryptPasswordEncoder.encode(myAdminPassword);
        SystemUser systemUser = new SystemUser(myAdminUsername, hashedPassword, "Marek", "Mistrz");

        List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
        systemUser.setAuthorities(new HashSet<>(authorities));

        systemUserRepository.save(systemUser);
    }
    
    public SystemUser findSystemUserById(long systemUserId) {
        return systemUserRepository.findById(systemUserId)
                .orElseThrow(() -> new SystemUserNotFoundException("No User found with the following ID: %d".formatted(systemUserId)));
    }
    
}