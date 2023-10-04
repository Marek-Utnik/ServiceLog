package com.servicedata.servicelogs.services;

import com.servicedata.servicelogs.exceptions.SystemUserNotFoundException;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.ConservationLogRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class ConservationLogService {

    private final SystemUserService systemUserService;
    private final ConservationLogRepository conservationLogRepository;
    
    public Page<ConservationLog> filteredConservationLog(Pageable pageable,
            												Machine machine,
            												String conservationDescription,
            												LocalDate publicationDateStart,
            												LocalDate publicationDateEnd,
            												Long systemUserId) {
        boolean conservationDecriptionCheck = conservationDescription != null && !conservationDescription.isBlank();
        boolean publicationDateStartCheck = publicationDateStart != null;
        boolean publicationDateEndCheck = publicationDateEnd != null;
        boolean systemUserIdCheck = systemUserId != null;

        Specification<ConservationLog> specification = Specification.where(null);

        if (conservationDecriptionCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("conservationDescription")), "%" + conservationDescription.toUpperCase() + "%"));
        }
        if (publicationDateStartCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("publicationDate"), publicationDateStart));
        }
        if (publicationDateEndCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), publicationDateEnd));
        }
        if (systemUserIdCheck) {
            try {
                SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
            	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("systemUser"), systemUser));

            } catch (SystemUserNotFoundException e) {
                e.printStackTrace();
            	SystemUser systemUser = null;
            	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("systemUser"), systemUser));
            }
        }
        
        Page<ConservationLog> page = conservationLogRepository.findAll(specification, pageable);    
        return page;
    }

}