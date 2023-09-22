    
    
package com.bazakonserwacji.zeszyt.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bazakonserwacji.zeszyt.exceptions.CompanyNotFoundException;
import com.bazakonserwacji.zeszyt.exceptions.SystemUserNotFoundException;
import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.ConservationLog;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.repositories.ConservationLogRepository;
import com.bazakonserwacji.zeszyt.repositories.SystemUserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
@Transactional
public class ConservationLogService {
    private final ConservationLogRepository conservationLogRepository;
    private final SystemUserRepository systemUserRepository;
    private final SystemUserService systemUserService;

    //Obiekt zarządzający Enitities
    private final EntityManager entityManager;
    
    public ConservationLogService(EntityManager entityManager, 
    		ConservationLogRepository conservationLogRepository, 
    		SystemUserRepository systemUserRepository,
    		SystemUserService systemUserService) {
    	this.entityManager = entityManager;
    	this.conservationLogRepository = conservationLogRepository;
        this.systemUserRepository = systemUserRepository;
        this.systemUserService = systemUserService;

    }    
    
    public Page<ConservationLog> filteredConservationLog(Pageable pageable, 
    		Machine machine, 
    		String conservationDescription, 
    		Date publicationDateStart, 
    		Date publicationDateEnd, 
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
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), conservationLogRoot, criteriaBuilder));

        List <ConservationLog> filteredConservationLogs = entityManager.createQuery(criteriaQuery).getResultList();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredConservationLogs.size());
        List<ConservationLog> pageContent = filteredConservationLogs.subList(start, end);        
        Page <ConservationLog> page = new PageImpl(pageContent, pageable, filteredConservationLogs.size());
        return page;
    }

    
}