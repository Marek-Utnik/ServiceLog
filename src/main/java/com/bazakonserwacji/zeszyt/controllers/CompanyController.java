package com.bazakonserwacji.zeszyt.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.repositories.MachineRepository;
import com.bazakonserwacji.zeszyt.repositories.SystemUserRepository;
import com.bazakonserwacji.zeszyt.services.CompanyService;
import com.bazakonserwacji.zeszyt.services.MachineService;

@Controller
@RequestMapping("/company")
public class CompanyController extends LoggedControllerSuper{

    private final CompanyService companyService;
    private final SystemUserRepository systemUserRepository;
    private final MachineRepository machineRepository;
    private final MachineService machineService;


    public CompanyController(
			  CompanyService companyService,
			  SystemUserRepository systemUserRepository,
		      MachineRepository machineRepository,
		      MachineService machineService
			  ) {
    	super(companyService);
  		this.companyService = companyService;
  		this.systemUserRepository = systemUserRepository;
  		this.machineRepository = machineRepository;
  		this.machineService = machineService;
  		}
    

 	  @RequestMapping("/{companyActive}/machine/add/")
	  public String companyAddMachineForm(Model model,
	              @PathVariable("companyActive") Long companyActive,
	              @ModelAttribute("companies") List<Company> companies,
           		  Authentication authentication) {
        	
        	Company company = companyService.findCompanyById(companyActive);
        	if (companies.contains(company))
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
              			@ModelAttribute("companies") List<Company> companies,
              			Authentication authentication) {
        	
        	Machine machine = machineService.findMachineById(machineId);
        	if (companies.contains(machine.getCompany()))
        	{
        		model.addAttribute("machine", machine);
    			return "company/machine-edit";
        	}
        	
        	
			return "error";
	  }
 	  
 	  @RequestMapping("/machine/update")
	  public String companyUpdateMachine(Model model,
			  		@ModelAttribute Machine machine,
			  		@ModelAttribute("companies") List<Company> companies,
           		  Authentication authentication) {
        	
        	Company company = machine.getCompany();
			if (companies.contains(company))
        	{
        		machineRepository.save(machine);
    			return "redirect:/machine/list/"+company.getCompanyId()+"/1";

        	}
        	
			return "error";
	  }
 	  
 	  @RequestMapping("/machine/delete/{machineId}")
	  public String companyDeleteMachine(Model model,
              			@PathVariable("machineId") Long machineId,
              			@ModelAttribute("companies") List<Company> companies,
              			Authentication authentication) {
	  		
        	
        	Machine machine = machineService.findMachineById(machineId);
        	Company company = machine.getCompany();
        	if (companies.contains(company))
        	{
     		  	machineRepository.delete(machine);
    			return "redirect:/machine/list/"+company.getCompanyId()+"/1";

        	}
        	
			return "error";
	  }


}