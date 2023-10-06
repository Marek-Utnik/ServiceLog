package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.models.SystemUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SystemUserRepository extends JpaRepository<SystemUser, Long>, JpaSpecificationExecutor<SystemUser>  {

    SystemUser findByUsername(String username);
    Page<SystemUser> findAll(Specification<SystemUser> spec, Pageable pageable);
}