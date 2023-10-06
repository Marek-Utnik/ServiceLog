package com.servicedata.servicelogs.repositories;

import com.servicedata.servicelogs.models.Machine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MachineRepository extends JpaRepository<Machine, Long>, JpaSpecificationExecutor<Machine>  {
    //	Optional<Machine> findById(long machineId);
    @Query("SELECT m FROM Machine m WHERE LOWER(m.company.companyName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Machine> findByCompanyNamePageable(Pageable pageable, @Param("name") String companyName);
    Page<Machine> findAll(Specification<Machine> spec, Pageable pageable);


}