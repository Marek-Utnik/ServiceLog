package com.bazakonserwacji.zeszyt.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bazakonserwacji.zeszyt.models.Authority;
import com.bazakonserwacji.zeszyt.models.ConservationLog;
import com.bazakonserwacji.zeszyt.models.Machine;

@Repository
public interface ConservationLogRepository  extends JpaRepository<ConservationLog, Long>,
											JpaSpecificationExecutor{
	List <ConservationLog> findByMachine(Machine machine);
	Page <ConservationLog> findByMachine(Pageable pageable, Machine machine);
	//Page <ConservationLog> findByMachineAndConservationDescription(Pageable pageable, Machine machine, String conservationDescription);
	//Page <ConservationLog> findByMachineAndConservationDescriptionAndPublication(Pageable pageable, Machine machine, String conservationDescription);
}