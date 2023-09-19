package com.bazakonserwacji.zeszyt.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.models.SystemUser;

@Repository
public interface MachineRepository  extends JpaRepository<Machine, Long>{
//	Optional<Machine> findById(long machineId);
    @Query("SELECT m FROM Machine m WHERE LOWER(m.company.companyName) LIKE LOWER(CONCAT('%', :name, '%'))")
	Page <Machine> findByCompanyNamePageable(Pageable pageable, @Param("name") String companyName);
	
	
}