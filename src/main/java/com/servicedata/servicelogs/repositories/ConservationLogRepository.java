package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ConservationLogRepository extends JpaRepository<ConservationLog, Long> {
    List<ConservationLog> findByMachine(Machine machine);

    Page<ConservationLog> findByMachine(Pageable pageable, Machine machine);
    Page<ConservationLog> findAll(Specification<ConservationLog> spec, Pageable pageable);

}