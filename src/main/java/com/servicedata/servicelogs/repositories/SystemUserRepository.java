package com.servicedata.servicelogs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicedata.servicelogs.models.SystemUser;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    SystemUser findByUsername(String username);
}