package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.enums.AuthorityName;
import com.servicedata.servicelogs.models.Authority;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface AuthorityRepository extends CrudRepository<Authority, Long> {

    Authority findByName(AuthorityName name);

    List<Authority> findAll();
}