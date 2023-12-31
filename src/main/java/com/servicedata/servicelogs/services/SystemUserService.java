package com.servicedata.servicelogs.services;

import com.servicedata.servicelogs.exceptions.SystemUserNotFoundException;
import com.servicedata.servicelogs.forms.SystemUserFilterData;
import com.servicedata.servicelogs.models.Authority;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.AuthorityRepository;
import com.servicedata.servicelogs.repositories.SystemUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //@Value("${my.admin.username}")
    private final String myAdminUsername = "admin2";

    //@Value("${my.admin.password}")
    private final String myAdminPassword = "admin2";

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

        List<Authority> authorities = authorityRepository.findAll();
        systemUser.setAuthorities(new HashSet<>(authorities));

        systemUserRepository.save(systemUser);
    }

    public SystemUser findSystemUserById(long systemUserId) {
        return systemUserRepository.findById(systemUserId)
                .orElseThrow(() -> new SystemUserNotFoundException("No User found with the following ID: %d".formatted(systemUserId)));
    }

    public Page<SystemUser> filteredSystemUser(Pageable pageable,
                                               SystemUserFilterData filterData
    ) {
        boolean systemUserIdCheck = filterData.getSystemUserId() != null;
        boolean usernameCheck = filterData.getUsername() != null && !filterData.getUsername().isBlank();
        boolean nameCheck = filterData.getName() != null && !filterData.getName().isBlank();
        boolean surnameCheck = filterData.getSurname() != null && !filterData.getSurname().isBlank();

        Specification<SystemUser> specification = Specification.where(null);

        if (systemUserIdCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("systemUserId"), filterData.getSystemUserId()));
        }
        if (usernameCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("username")), "%" + filterData.getUsername().toUpperCase() + "%"));
        }
        if (nameCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), "%" + filterData.getName().toUpperCase() + "%"));
        }
        if (surnameCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("surname")), "%" + filterData.getSurname().toUpperCase() + "%"));
        }
        
        Page<SystemUser> page = systemUserRepository.findAll(specification, pageable);    

        return page;
    }

}