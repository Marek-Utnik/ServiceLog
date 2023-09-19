package com.bazakonserwacji.zeszyt.controllers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.ConservationLog;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.repositories.AuthorityRepository;
import com.bazakonserwacji.zeszyt.repositories.CompanyRepository;
import com.bazakonserwacji.zeszyt.repositories.ConservationLogRepository;
import com.bazakonserwacji.zeszyt.repositories.MachineRepository;
import com.bazakonserwacji.zeszyt.repositories.SystemUserRepository;
import com.bazakonserwacji.zeszyt.services.CompanyService;
import com.bazakonserwacji.zeszyt.services.MachineService;
import com.bazakonserwacji.zeszyt.services.SystemUserDetailsService;
import com.bazakonserwacji.zeszyt.services.SystemUserService;

@Controller
@RequestMapping("/service")
public class ServiceController{

    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final SystemUserRepository systemUserRepository;
    private final SystemUserService systemUserService;
    private final SystemUserDetailsService systemUserDetailsService;
    private final AuthorityRepository authorityRepository;
    private final MachineRepository machineRepository;
    private final MachineService machineService;
    private final ConservationLogRepository conservationLogRepository;

    public ServiceController(
			  CompanyRepository companyRepository,
			  CompanyService companyService,
			  SystemUserRepository systemUserRepository,
			  SystemUserService systemUserService,
			  SystemUserDetailsService systemUserDetailsService,
			  AuthorityRepository authorityRepository,
		      MachineRepository machineRepository,
		      MachineService machineService,
		      ConservationLogRepository conservationLogRepository
			  ) {
  		this.companyRepository = companyRepository;
  		this.companyService = companyService;
  		this.systemUserRepository = systemUserRepository;
  		this.systemUserService = systemUserService;
  		this.authorityRepository = authorityRepository;
  		this.systemUserDetailsService = systemUserDetailsService;
  		this.machineRepository = machineRepository;
  		this.machineService = machineService;
  		this.conservationLogRepository = conservationLogRepository;
  		}
    
      @RequestMapping("/log/add/{machineId}")
	  public String serviceAddLogForm(Model model,
            			@PathVariable("machineId") Long machineId,
            			Authentication authentication) {
	  		
	  	String systemUserName = authentication.getName();
  		SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
  		String role = "ROLE_ADMIN";
  		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
  		Set<Company> companies = (isAdmin) ? new HashSet<Company>(companyRepository.findAll()) : systemUser.getCompanies();
      	model.addAttribute("companies", companies);
      	
      	Machine machine = machineService.findMachineById(machineId);
      	ConservationLog conservationLog = new ConservationLog();
      	
      	if (systemUser.getCompanies().contains(machine.getCompany()))
      	{
      		conservationLog.setSystemUser(systemUser);
      		conservationLog.setMachine(machine);
      		model.addAttribute("conservationLog",conservationLog);
  			return "service/conservation-add";

      	}   	
			return "error";
	  }
      
 	  @RequestMapping("/log/save")
	  public String serviceSaveLog(Model model,
			  		@ModelAttribute ConservationLog conservationLog,
           		  Authentication authentication) {
	  		
	  		String systemUserName = authentication.getName();
    		SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        	Company company = conservationLog.getMachine().getCompany();

        	System.out.println(conservationLog);
        	System.out.println(systemUser);
        	System.out.println(company);

        	
			if (systemUser.getCompanies().contains(company))
        	{
	      		conservationLog.setPublicationDate(new Date());
        		conservationLogRepository.save(conservationLog);
    			return "redirect:/machine/details/"+conservationLog.getMachine().getMachineId();
        	}
        	
			return "index";
	  }
 }
    