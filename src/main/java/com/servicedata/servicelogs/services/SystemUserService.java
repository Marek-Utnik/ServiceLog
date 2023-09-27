package com.servicedata.servicelogs.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.servicedata.servicelogs.exceptions.SystemUserNotFoundException;
import com.servicedata.servicelogs.models.Authority;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.AuthorityRepository;
import com.servicedata.servicelogs.repositories.SystemUserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class SystemUserService {

    private final SystemUserRepository systemUserRepository;
    private final AuthorityRepository authorityRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EntityManager entityManager;

    //@Value("${my.admin.username}")
    private String myAdminUsername = "admin2";

    //@Value("${my.admin.password}")
    private String myAdminPassword ="admin2";

    public SystemUserService(SystemUserRepository systemUserRepository, AuthorityRepository authorityRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EntityManager entityManager) {
        this.systemUserRepository = systemUserRepository;
        this.authorityRepository = authorityRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.entityManager=  entityManager;
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
    
    public Page<SystemUser> filteredSystemUser(Pageable pageable, 
    		Long systemUserId, 
    		String username,
    		String name,
    		String surname
    		){
        boolean systemUserIdCheck = systemUserId != null;
        boolean usernameCheck = username != null && !username.isBlank();
        boolean nameCheck = name != null && !name.isBlank();
        boolean surnameCheck = surname != null && !surname.isBlank();

        //Obiekt budujący zapytanie do bazy
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //Obiekt zapytania do bazy - jakie będą zwracane obiekty
        CriteriaQuery<SystemUser> criteriaQuery = criteriaBuilder.createQuery(SystemUser.class);
        //Gdzie będziemy szukać
        Root<SystemUser> systemUserRoot = criteriaQuery.from(SystemUser.class);
        //Określamy że to będzie select
        CriteriaQuery<SystemUser> select = criteriaQuery.select(systemUserRoot);
        //Pusta lista warunków
        List<Predicate> predicates = new ArrayList<>();
        
        
        if (systemUserIdCheck) {
            predicates.add(criteriaBuilder.equal(systemUserRoot.get("systemUserId"), systemUserId));
        }
        if (usernameCheck) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(systemUserRoot.get("username")), "%" + username.toUpperCase() + "%"));
        }
        if (nameCheck) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(systemUserRoot.get("name")), "%" + name.toUpperCase() + "%"));
        }
        if (surnameCheck) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(systemUserRoot.get("surname")), "%" + surname.toUpperCase() + "%"));
        }
        select.where(predicates.toArray(Predicate[]::new));
        select.orderBy(QueryUtils.toOrders(pageable.getSort(), systemUserRoot, criteriaBuilder));

        List <SystemUser> filteredSystemUser = entityManager.createQuery(select).getResultList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredSystemUser.size());
        List<SystemUser> pageContent = filteredSystemUser.subList(start, end);        
        Page <SystemUser> page = new PageImpl<SystemUser>(pageContent, pageable, filteredSystemUser.size());
        return page;
    }
    
}