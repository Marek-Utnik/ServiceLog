package com.servicedata.servicelogs;

import com.servicedata.servicelogs.enums.AuthorityName;
import com.servicedata.servicelogs.models.Authority;
import com.servicedata.servicelogs.repositories.AuthorityRepository;
import com.servicedata.servicelogs.services.SystemUserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class Loader implements InitializingBean {


    private final AuthorityRepository authorityRepository;
    private final SystemUserService systemUserService;

    public Loader(AuthorityRepository authorityRepository, SystemUserService systemUserService) {
        this.authorityRepository = authorityRepository;
        this.systemUserService = systemUserService;
    }

    @Override
    public void afterPropertiesSet() {
        prepareAuthorities();
        systemUserService.prepareAdmin();
    }

    private void prepareAuthorities() {
        for (AuthorityName name : AuthorityName.values()) {
            Authority existingAuthority = authorityRepository.findByName(name);
            if (existingAuthority == null) {
                Authority authority = new Authority(name);
                authorityRepository.save(authority);
            }
        }
    }

}