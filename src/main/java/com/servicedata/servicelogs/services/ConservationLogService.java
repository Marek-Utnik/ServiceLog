package com.servicedata.servicelogs.services;

import com.servicedata.servicelogs.forms.ConservationLogFilterData;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.ConservationLogRepository;

import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ConservationLogService {

    private final ConservationLogRepository conservationLogRepository;
    
    public Page<ConservationLog> filteredConservationLog(Pageable pageable,
            												Machine machine,
            												ConservationLogFilterData filterData) {
        boolean conservationDecriptionCheck = filterData.getConservationDescription() != null && !filterData.getConservationDescription().isBlank();
        boolean publicationDateStartCheck = filterData.getPublicationDateStart() != null;
        boolean publicationDateEndCheck = filterData.getPublicationDateEnd() != null;
        boolean systemUserIdCheck = filterData.getSystemUserId() != null;
        Specification<ConservationLog> specification = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("machine"), machine));
        log.info("System User Id: {}", filterData.getSystemUserId());
        if (conservationDecriptionCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("conservationDescription")), "%" + filterData.getConservationDescription().toUpperCase() + "%"));
        }
        if (publicationDateStartCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("publicationDate"), filterData.getPublicationDateStart()));
        }
        if (publicationDateEndCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), filterData.getPublicationDateEnd()));
        }
        if (systemUserIdCheck) {
        	specification = specification.and((root, query, criteriaBuilder) -> {
        		Join<SystemUser, ConservationLog> conservationLogSystemUser = root.join("systemUser");
        		return criteriaBuilder.equal(conservationLogSystemUser.get("systemUserId"), filterData.getSystemUserId());
        	});
        }
        
        Page<ConservationLog> page = conservationLogRepository.findAll(specification, pageable);    
        return page;
    }
    
    public List<ConservationLog> filteredConservationLogExcel(Machine machine,
															  ConservationLogFilterData filterData) {
    	boolean publicationDateStartCheck = filterData.getPublicationDateStart() != null;
    	boolean publicationDateEndCheck = filterData.getPublicationDateEnd() != null;
    	Specification<ConservationLog> specification = Specification.where((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("machine"), machine));

    	if (publicationDateStartCheck) {
    		specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("publicationDate"), filterData.getPublicationDateStart()));
    	}
    	if (publicationDateEndCheck) {
    		specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), filterData.getPublicationDateEnd()));
    	}

    	List<ConservationLog> conservationLogList = conservationLogRepository.findAll(specification);    
    	return conservationLogList;
    }

}