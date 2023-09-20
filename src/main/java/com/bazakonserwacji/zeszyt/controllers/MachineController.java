package com.bazakonserwacji.zeszyt.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bazakonserwacji.zeszyt.models.Authority;
import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.ConservationLog;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.models.SystemUser;
import com.bazakonserwacji.zeszyt.repositories.CompanyRepository;
import com.bazakonserwacji.zeszyt.repositories.ConservationLogRepository;
import com.bazakonserwacji.zeszyt.repositories.MachineRepository;
import com.bazakonserwacji.zeszyt.repositories.SystemUserRepository;
import com.bazakonserwacji.zeszyt.services.CompanyService;
import com.bazakonserwacji.zeszyt.services.MachineService;


@Controller
@RequestMapping("/machine")
public class MachineController{
    private final MachineService machineService;
    private final SystemUserRepository systemUserRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;

    private final MachineRepository machineRepository;
    private final ConservationLogRepository conservationLogRepository;

    
	public MachineController(MachineService machineService,
							SystemUserRepository systemUserRepository, 
							CompanyRepository companyRepository,
							MachineRepository machineRepository,
							CompanyService companyService,
							ConservationLogRepository conservationLogRepository
			) {
		this.machineService = machineService;
		this.systemUserRepository = systemUserRepository;
		this.companyRepository = companyRepository;
		this.machineRepository = machineRepository;
		this.companyService = companyService;
		this.conservationLogRepository = conservationLogRepository;
	}


    @GetMapping("/list")
    public String getCompanyName(Model model,
                            Authentication authentication
                            ) {
    	
    	if (authentication != null) {
    		System.out.println(authentication.getAuthorities());
        	String systemUserName = authentication.getName();
        	SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        	String role = "ROLE_ADMIN";
        	boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
        	Set<Company> companies = (isAdmin) ? new HashSet<Company>(companyRepository.findAll()) : systemUser.getCompanies();
            model.addAttribute("companies", companies);
          	}
    	
        return "machine/list";
    }
    
    @GetMapping("/list/{companyId}/{page}")
    public String getAllMachines(Model model,
                            @SortDefault("machineId") Pageable pageable,
                            @PathVariable("companyId") Long companyId,
                            @PathVariable("page") int page,
                            Authentication authentication
                            ) {
    	String systemUserName = authentication.getName();
    	SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
    	String role = "ROLE_ADMIN";
    	boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
    	Set<Company> companies = (isAdmin) ? new HashSet<Company>(companyRepository.findAll()) : systemUser.getCompanies();
        model.addAttribute("companies", companies);

        Page<Machine> machinesPaged = null;
        Company company = companyService.findCompanyById(companyId);

        if (companies.contains(company))
        {

        	
        	
        	pageable = PageRequest.of(page-1, 2);
        	machinesPaged = machineRepository.findByCompanyNamePageable(pageable, company.getCompanyName());
        	int totalPages = machinesPaged.getTotalPages();
        	if(totalPages > 0) {
        		List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
        		model.addAttribute("pageNumbers", pageNumbers);
        	}
            model.addAttribute("machines", machinesPaged);
            model.addAttribute("companyActive", companyId);
            return "machine/list";
        }
        return "error";
    	
    }

    @GetMapping("/details/{machineId}")    
    public String getMachineDetails(Model model,
            @PathVariable("machineId") Long machineId,
            Authentication authentication
            ) {
    	String systemUserName = authentication.getName();
    	SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
    	String role = "ROLE_ADMIN";
    	boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role));
    	Set<Company> companies = (isAdmin) ? new HashSet<Company>(companyRepository.findAll()) : systemUser.getCompanies();
        model.addAttribute("companies", companies);
        
        Machine machine = machineService.findMachineById(machineId);
        Company company = machine.getCompany();
        List <ConservationLog> logs = conservationLogRepository.findByMachine(machine);
        if (companies.contains(company))
        {
        	model.addAttribute("logs", logs);
        	model.addAttribute("machine", machine);
        	return "machine/details";
        }
        return "error";	
    }
    
//	@PostMapping("/save")
//	public Machine save	(@RequestParam int registrationNumber,
//					     @RequestParam int serialNumber,
//					     @RequestParam String producerName,
//					     @RequestParam String machineType)
//	{
//	Machine machine = new Machine(registrationNumber, serialNumber,producerName,machineType);
//	return machineRepository.save(machine);
//	}
	
}

