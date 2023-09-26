package com.servicedata.servicelogs.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.servicedata.servicelogs.enums.AuthorityName;
import com.servicedata.servicelogs.models.Authority;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findByName(AuthorityName name);
    List<Authority> findAll();
}