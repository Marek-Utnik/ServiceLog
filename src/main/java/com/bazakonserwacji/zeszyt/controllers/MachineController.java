package com.bazakonserwacji.zeszyt.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bazakonserwacji.zeszyt.models.Company;
import com.bazakonserwacji.zeszyt.models.ConservationLog;
import com.bazakonserwacji.zeszyt.models.Machine;
import com.bazakonserwacji.zeszyt.repositories.ConservationLogRepository;
import com.bazakonserwacji.zeszyt.repositories.MachineRepository;
import com.bazakonserwacji.zeszyt.services.CompanyService;
import com.bazakonserwacji.zeszyt.services.MachineService;


@Controller
@RequestMapping("/machine")
public class MachineController extends LoggedControllerSuper{
    private final MachineService machineService;
    private final CompanyService companyService;

    private final MachineRepository machineRepository;
    private final ConservationLogRepository conservationLogRepository;

    
	public MachineController(MachineService machineService,
							MachineRepository machineRepository,
							CompanyService companyService,
							ConservationLogRepository conservationLogRepository
			) {
		super(companyService);
		this.companyService = companyService;
		this.machineService = machineService;
		this.machineRepository = machineRepository;
		this.conservationLogRepository = conservationLogRepository;
	}
	

    @GetMapping("/list")
    public String getCompanyName(Model model,
                            Authentication authentication
                            ) {
    	return "machine/list";
    }
    
    @GetMapping("/list/{companyId}/{page}")
    public String getAllMachines(Model model,
                            @SortDefault("machineId") Pageable pageable,
                            @ModelAttribute("companies") List<Company> companies,
                            @PathVariable("companyId") Long companyId,
                            @PathVariable("page") int page,
                            Authentication authentication
                            ) {

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
            @ModelAttribute("companies") List<Company> companies,
            Authentication authentication
            ) {
        
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
	
}

