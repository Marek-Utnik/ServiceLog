package com.bazakonserwacji.zeszyt.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.bazakonserwacji.zeszyt.models.Authority;
import com.bazakonserwacji.zeszyt.models.SystemUser;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    SystemUser findByUsername(String username);
}