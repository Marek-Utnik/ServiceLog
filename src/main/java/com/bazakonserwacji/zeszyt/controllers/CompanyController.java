package com.bazakonserwacji.zeszyt.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.repositories.AuthorityRepository;
import com.bazakonserwacji.zeszyt.repositories.CompanyRepository;
import com.bazakonserwacji.zeszyt.repositories.MachineRepository;
import com.bazakonserwacji.zeszyt.repositories.SystemUserRepository;
import com.bazakonserwacji.zeszyt.services.CompanyService;
import com.bazakonserwacji.zeszyt.services.MachineService;
import com.bazakonserwacji.zeszyt.services.SystemUserDetailsService;
import com.bazakonserwacji.zeszyt.services.SystemUserService;

@Controller
@RequestMapping("/company")
public class CompanyController{

    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final SystemUserRepository systemUserRepository;
    private final SystemUserService systemUserService;
    private final SystemUserDetailsService systemUserDetailsService;
    private final AuthorityRepository authorityRepository;
    private final MachineRepository machineRepository;
    private final MachineService machineService;


    public CompanyController(
			  CompanyRepository companyRepository,
			  CompanyService companyService,
			  SystemUserRepository systemUserRepository,
			  SystemUserService systemUserService,
			  SystemUserDetailsService systemUserDetailsService,
			  AuthorityRepository authorityRepository,
		      MachineRepository machineRepository,
		      MachineService machineService
			  ) {
  		this.companyRepository = companyRepository;
  		this.companyService = companyService;
  		this.systemUserRepository = systemUserRepository;
  		this.systemUserService = systemUserService;
  		this.authorityRepository = authorityRepository;
  		this.systemUserDetailsService = systemUserDetailsService;
  		this.machineRepository = machineRepository;
  		this.machineService = machineService;
  		}
    

 	  @RequestMapping("/{companyActive}/machine/add/")
	  public String companyAddMachineForm(Model model,
	              @PathVariable("companyActive") Long companyActive,
           		  Authentication authentication) {
	  		
	  		String systemUserName = authentication.getName();
    		SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
    		String role = "ROLE_ADMIN";
    		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
    		Set<Company> companies = (isAdmin) ? new HashSet<Company>(companyRepository.findAll()) : systemUser.getCompanies();
        	model.addAttribute("companies", companies);
        	
        	Company company = companyService.findCompanyById(companyActive);
        	if (systemUser.getCompanies().contains(company))
        	{
        		Machine machine = new Machine();
        		machine.setCompany(company);
        		model.addAttribute("machine", machine);
    			return "company/machine-add";

        	}
        	
			return "error";
	  }
 	  @RequestMapping("/{companyActive}/machine/save")
	  public String companyAddMachine(Model model,
			  		@ModelAttribute Machine machine,
           		  Authentication authentication) {
	  		
	  		String systemUserName = authentication.getName();
    		SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        	
        	Company company = machine.getCompany();
			if (systemUser.getCompanies().contains(company))
        	{
        		machineRepository.save(machine);
    			return "redirect:/machine/list/"+company.getCompanyId()+"/1";

        	}
        	
			return "error";
	  }
 	  
 	  @RequestMapping("/machine/edit/{machineId}")
	  public String companyEditMachine(Model model,
              			@PathVariable("machineId") Long machineId,
              			Authentication authentication) {
	  		
	  		String systemUserName = authentication.getName();
    		SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
    		String role = "ROLE_ADMIN";
    		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
    		Set<Company> companies = (isAdmin) ? new HashSet<Company>(companyRepository.findAll()) : systemUser.getCompanies();
        	model.addAttribute("companies", companies);
        	
        	Machine machine = machineService.findMachineById(machineId);
        	if (systemUser.getCompanies().contains(machine.getCompany()))
        	{
        		model.addAttribute("machine", machine);
    			return "company/machine-edit";

        	}
        	
        	
			return "error";
	  }
 	  
 	  @RequestMapping("/machine/update")
	  public String companyUpdateMachine(Model model,
			  		@ModelAttribute Machine machine,
           		  Authentication authentication) {
	  		
	  		String systemUserName = authentication.getName();
    		SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        	
        	Company company = machine.getCompany();
			if (systemUser.getCompanies().contains(company))
        	{
        		machineRepository.save(machine);
    			return "redirect:/machine/list/"+company.getCompanyId()+"/1";

        	}
        	
			return "error";
	  }
 	  
 	  @RequestMapping("/machine/delete/{machineId}")
	  public String companyDeleteMachine(Model model,
              			@PathVariable("machineId") Long machineId,
              			Authentication authentication) {
	  		
	  		String systemUserName = authentication.getName();
    		SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        	
        	Machine machine = machineService.findMachineById(machineId);
        	Company company = machine.getCompany();
        	if (systemUser.getCompanies().contains(company))
        	{
     		  	machineRepository.delete(machine);
    			return "redirect:/machine/list/"+company.getCompanyId()+"/1";

        	}
        	
			return "error";
	  }


}