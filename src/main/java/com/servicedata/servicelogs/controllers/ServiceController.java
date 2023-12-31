package com.servicedata.servicelogs.controllers;

import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.ConservationLog;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.ConservationLogRepository;
import com.servicedata.servicelogs.repositories.SystemUserRepository;
import com.servicedata.servicelogs.services.CompanyService;
import com.servicedata.servicelogs.services.MachineService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/service")
public class ServiceController extends LoggedControllerSuper {

    private final SystemUserRepository systemUserRepository;
    private final MachineService machineService;
    private final ConservationLogRepository conservationLogRepository;

    public ServiceController(
            CompanyService companyService,
            SystemUserRepository systemUserRepository,
            MachineService machineService,
            ConservationLogRepository conservationLogRepository) {
        super(companyService);
        this.systemUserRepository = systemUserRepository;
        this.machineService = machineService;
        this.conservationLogRepository = conservationLogRepository;
    }

    @RequestMapping("/log/add/{machineId}")
    public String serviceAddLogForm(Model model,
                                    @PathVariable("machineId") Long machineId,
                                    @ModelAttribute("companies") List<Company> companies,
                                    Authentication authentication) {
        String systemUserName = authentication.getName();
        SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        Machine machine = machineService.findMachineById(machineId);
        ConservationLog conservationLog = new ConservationLog();
        if (companies.contains(machine.getCompany())) {
            conservationLog.setSystemUser(systemUser);
            conservationLog.setMachine(machine);
            model.addAttribute("conservationLog", conservationLog);
            return "service/conservation-add";
        }
        return "error";
    }

    @RequestMapping("/log/save")
    public String serviceSaveLog(@ModelAttribute ConservationLog conservationLog,
                                 Authentication authentication) {
        String systemUserName = authentication.getName();
        SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        Company company = conservationLog.getMachine().getCompany();
        log.info("System user: {}",systemUserName);
        if (systemUser.getCompanies().contains(company)) {
            conservationLog.setPublicationDate(LocalDate.now());
            conservationLogRepository.save(conservationLog);
            return "redirect:/machine/details/" + conservationLog.getMachine().getMachineId();
        }
        return "index";
    }

}
    