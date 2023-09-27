    
    
package com.servicedata.servicelogs.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicedata.servicelogs.exceptions.SystemUserNotFoundException;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.models.SystemUser;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional
public class ConservationLogService {
	
    private final SystemUserService systemUserService;

    //Obiekt zarządzający Enitities
    private final EntityManager entityManager;
    
    public ConservationLogService(EntityManager entityManager, 
    		SystemUserService systemUserService) {
    	this.entityManager = entityManager;
        this.systemUserService = systemUserService;

    }    
    
    public Page<ConservationLog> filteredConservationLog(Pageable pageable, 
    		Machine machine, 
    		String conservationDescription, 
    		LocalDate publicationDateStart, 
    		LocalDate publicationDateEnd, 
    		Long systemUserId ){
        boolean conservationDecriptionCheck = conservationDescription != null && !conservationDescription.isBlank();
        boolean publicationDateStartCheck = publicationDateStart != null;
        boolean publicationDateEndCheck = publicationDateEnd != null;
        boolean systemUserIdCheck = systemUserId != null;

        //Obiekt budujący zapytanie do bazy
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        //Obiekt zapytania do bazy - jakie będą zwracane obiekty
        CriteriaQuery<ConservationLog> criteriaQuery = criteriaBuilder.createQuery(ConservationLog.class);
        //Gdzie będziemy szukać
        Root<ConservationLog> conservationLogRoot = criteriaQuery.from(ConservationLog.class);
        //Określamy że to będzie select
        CriteriaQuery<ConservationLog> select = criteriaQuery.select(conservationLogRoot);
        //Pusta lista warunków
        List<Predicate> predicates = new ArrayList<>();
        
        
        predicates.add(criteriaBuilder.equal(conservationLogRoot.get("machine"), machine));
        if (conservationDecriptionCheck) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.upper(conservationLogRoot.get("conservationDescription")), "%" + conservationDescription.toUpperCase() + "%"));
        }
        if (publicationDateStartCheck) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(conservationLogRoot.get("publicationDate"), publicationDateStart));
        }
        if (publicationDateEndCheck) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(conservationLogRoot.get("publicationDate"), publicationDateEnd));
        }
        if (systemUserIdCheck) {
        	try {
        		SystemUser systemUser = systemUserService.findSystemUserById(systemUserId);
                predicates.add(criteriaBuilder.equal(conservationLogRoot.get("systemUser"), systemUser));
        	}
        	catch(SystemUserNotFoundException e)
        	{
        		e.printStackTrace();
        		SystemUser systemUser = null;
                predicates.add(criteriaBuilder.equal(conservationLogRoot.get("systemUser"), systemUser));
        	}
        	
        }
        select.where(predicates.toArray(Predicate[]::new));
        select.orderBy(QueryUtils.toOrders(pageable.getSort(), conservationLogRoot, criteriaBuilder));

        List <ConservationLog> filteredConservationLogs = entityManager.createQuery(select).getResultList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredConservationLogs.size());
        List<ConservationLog> pageContent = filteredConservationLogs.subList(start, end);        
        Page <ConservationLog> page = new PageImpl<ConservationLog>(pageContent, pageable, filteredConservationLogs.size());
        return page;
    }

    
}