package com.servicedata.servicelogs.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;

@Repository
public interface ConservationLogRepository  extends JpaRepository<ConservationLog, Long>{
	List <ConservationLog> findByMachine(Machine machine);
	Page <ConservationLog> findByMachine(Pageable pageable, Machine machine);
}