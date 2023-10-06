package com.servicedata.servicelogs.controllers;

import com.servicedata.servicelogs.forms.ConservationLogFilterData;
import com.servicedata.servicelogs.forms.MachineFilterData;
import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.services.CompanyService;
import com.servicedata.servicelogs.services.ConservationLogService;
import com.servicedata.servicelogs.services.MachineService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@Slf4j
@RequestMapping("/machine")
public class MachineController extends LoggedControllerSuper {

    private final MachineService machineService;
    private final CompanyService companyService;
    private final ConservationLogService conservationLogService;

    public MachineController(MachineService machineService,
                             CompanyService companyService,
                             ConservationLogService conservationLogService) {
        super(companyService);
        this.companyService = companyService;
        this.machineService = machineService;
        this.conservationLogService = conservationLogService;
    }

    @GetMapping("/list")
    public String getCompanyName(Model model,
                                 @SortDefault("machineId") Pageable pageable,
                                 @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                 Authentication authentication) {
        Page<Machine> machinesPaged = null;
        model.addAttribute("machines", machinesPaged);
        return "machine/list";
    }

    @GetMapping("/list/{companyId}")
    public String getAllMachines(Model model,
                                 @ModelAttribute("companies") List<Company> companies,
                                 @PathVariable("companyId") Long companyId,
                                 @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                 @SortDefault("machineId") Pageable pageable,
                                 @ModelAttribute("filterData") MachineFilterData filterData,
                                 Sort sort,
                                 Authentication authentication) {
        Company company = companyService.findCompanyById(companyId);
        if (companies.contains(company)) {
            pageable = PageRequest.of(page - 1, 4, sort);
            Page<Machine> machinesPaged = machineService.filteredMachine(pageable, company, filterData);
            int totalPages = machinesPaged.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            model.addAttribute("machines", machinesPaged);
            model.addAttribute("companyActive", companyId);
            return "machine/list";
        }
        return "error";
    }

    @RequestMapping("/details/{machineId}")
    public String getMachineDetailsNew(Model model,
                                    @PathVariable("machineId") Long machineId,
                                    @ModelAttribute("filterData") ConservationLogFilterData filterData,
                                    @ModelAttribute("companies") List<Company> companies,
                                    @SortDefault("systemUserId") Pageable pageable,
                                    @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                    Sort sort,
                                    Authentication authentication) {
        Machine machine = machineService.findMachineById(machineId);
        Company company = machine.getCompany();
        if (companies.contains(company)) {
            pageable = PageRequest.of(page - 1, 4, sort);
            Page<ConservationLog> logs = conservationLogService.filteredConservationLog(pageable, machine, filterData);
            int totalPages = logs.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            log.info("Total pages: {}", totalPages);
            model.addAttribute("filterData", filterData);
            model.addAttribute("logs", logs);
            model.addAttribute("machine", machine);
            model.addAttribute("sort", sort);
            log.info("Data: {}", model.getAttribute("filterData"));
            
            
            return "machine/details";
        }
        return "error";
    }

    
}

