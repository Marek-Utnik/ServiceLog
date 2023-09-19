package com.bazakonserwacji.zeszyt;

import com.bazakonserwacji.zeszyt.models.Authority;
import com.bazakonserwacji.zeszyt.enums.AuthorityName;
import com.bazakonserwacji.zeszyt.repositories.AuthorityRepository;
import com.bazakonserwacji.zeszyt.services.SystemUserService;
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

    private void prepareAuthorities()  {
        for (AuthorityName name : AuthorityName.values()) {
            Authority existingAuthority = authorityRepository.findByName(name);
            if (existingAuthority == null) {
                Authority authority = new Authority(name);
                authorityRepository.save(authority);
            }
        }
    }
    
}