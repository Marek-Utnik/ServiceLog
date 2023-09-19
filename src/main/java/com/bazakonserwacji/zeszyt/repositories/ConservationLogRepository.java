package com.bazakonserwacji.zeszyt.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bazakonserwacji.zeszyt.models.Authority;
import com.bazakonserwacji.zeszyt.models.ConservationLog;
import com.bazakonserwacji.zeszyt.models.Machine;

@Repository
public interface ConservationLogRepository  extends JpaRepository<ConservationLog, Long>{
	List<ConservationLog> findByMachine(Machine machine);
}