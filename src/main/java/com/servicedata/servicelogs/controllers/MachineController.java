package com.servicedata.servicelogs.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.services.CompanyService;
import com.servicedata.servicelogs.services.ConservationLogService;
import com.servicedata.servicelogs.services.MachineService;


@Controller
@RequestMapping("/machine")
public class MachineController extends LoggedControllerSuper{
    private final MachineService machineService;
    private final CompanyService companyService;

    private final ConservationLogService conservationLogService;

    
	public MachineController(MachineService machineService,
							CompanyService companyService,
							ConservationLogService conservationLogService
			) {
		super(companyService);
		this.companyService = companyService;
		this.machineService = machineService;
		this.conservationLogService = conservationLogService;
	}
	

    @GetMapping("/list")
    public String getCompanyName(Model model,
            				@SortDefault("machineId") Pageable pageable,
            	            @RequestParam(value = "page", defaultValue="1", required = false) int page,
                            Authentication authentication
                            ) {
        Page<Machine> machinesPaged = null;
        model.addAttribute("machines", machinesPaged);
        
    	return "machine/list";
    }
    
    @GetMapping("/list/{companyId}")
    public String getAllMachines(Model model,
                            @ModelAttribute("companies") List<Company> companies,
                            @PathVariable("companyId") Long companyId,
                            @RequestParam(value = "page", defaultValue="1", required = false) int page,
                            @SortDefault("machineId") Pageable pageable,
            	            @RequestParam(value = "machineId", required = false) Long machineId,
            	            @RequestParam(value = "registrationNumber", required = false) Integer registrationNumber,
            	            @RequestParam(value = "serialNumber", required = false) Integer serialNumber,
            	            @RequestParam(value = "producerName", required = false) String producerName,
            	            @RequestParam(value = "machineType", required = false) String machineType,
                            Sort sort,
                            Authentication authentication
                            ) {

        	
        Company company = companyService.findCompanyById(companyId);
        if (companies.contains(company))
        {
        	pageable = PageRequest.of(page-1,4,sort);
            Page <Machine> machinesPaged = machineService.filteredMachine(pageable, company,machineId,registrationNumber,serialNumber,producerName,machineType);
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

    @RequestMapping("/details/{machineId}")    
    public String getMachineDetails(Model model,
            @PathVariable("machineId") Long machineId,
            @ModelAttribute("companies") List<Company> companies,
            @RequestParam(value = "systemUserId", required = false) Long systemUserId,
            @RequestParam(value = "publicationDateStart", required = false) String pDStart,
            @RequestParam(value = "publicationDateEnd", required = false) String pDEnd,
            @RequestParam(value = "conservationDescription", required = false) String conservationDescription,
            @SortDefault("systemUserId") Pageable pageable,
            @RequestParam(value = "page", defaultValue="1", required = false) int page,
            Sort sort,
            Authentication authentication
            ) {
    	
    	Date publicationDateStart = null;
    	Date publicationDateEnd = null;
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		if (pDStart!=null) {
    			if (!pDStart.isEmpty()) {
    			publicationDateStart = df.parse(pDStart);
    			}
    		}
    		if (pDEnd!=null) {
    			if (!pDEnd.isEmpty()) {
    			publicationDateEnd = df.parse(pDEnd);
    			}
    		}
    	}
    	catch(ParseException e) {
    	    e.printStackTrace();
    	}


        Machine machine = machineService.findMachineById(machineId);
        Company company = machine.getCompany();
        
        
        if (companies.contains(company))
        {
        	pageable = PageRequest.of(page-1,4,sort);
            Page <ConservationLog> logs = conservationLogService.filteredConservationLog(pageable, machine,conservationDescription,publicationDateStart,publicationDateEnd,systemUserId);
        	int totalPages = logs.getTotalPages();
        	if(totalPages > 0) {
        		List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
        		model.addAttribute("pageNumbers", pageNumbers);
        	}
        	model.addAttribute("logs", logs);
        	model.addAttribute("machine", machine);
        	model.addAttribute("sort", sort);

        	return "machine/details";
        }
        return "error";	
    }
	
}

