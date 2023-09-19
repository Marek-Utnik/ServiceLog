package com.bazakonserwacji.zeszyt.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bazakonserwacji.zeszyt.enums.AuthorityName;
import com.bazakonserwacji.zeszyt.models.Authority;

public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findByName(AuthorityName name);
    List<Authority> findAll();
}