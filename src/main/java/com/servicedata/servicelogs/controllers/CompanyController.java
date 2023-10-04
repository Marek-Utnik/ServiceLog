package com.servicedata.servicelogs.controllers;

import com.servicedata.servicelogs.models.Company;
import com.servicedata.servicelogs.models.Machine;
import com.servicedata.servicelogs.models.SystemUser;
import com.servicedata.servicelogs.repositories.MachineRepository;
import com.servicedata.servicelogs.repositories.SystemUserRepository;
import com.servicedata.servicelogs.services.CompanyService;
import com.servicedata.servicelogs.services.MachineService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController extends LoggedControllerSuper {

    private final CompanyService companyService;
    private final SystemUserRepository systemUserRepository;
    private final MachineRepository machineRepository;
    private final MachineService machineService;

    public CompanyController(
            CompanyService companyService,
            SystemUserRepository systemUserRepository,
            MachineRepository machineRepository,
            MachineService machineService) {
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
        if (companies.contains(company)) {
            Machine machine = new Machine();
            machine.setCompany(company);
            model.addAttribute("machine", machine);
            return "company/machine-add";
        }
        return "error";
    }

    @RequestMapping("/{companyActive}/machine/add/save")
    public String companyAddMachine(Model model,
                                    @Valid Machine machine,
                                    Errors errors,
                                    Authentication authentication) {
        System.out.println(errors);
        if (errors.hasErrors()) {
            model.addAttribute("machine", machine);
            return "company/machine-add";
        }
        String systemUserName = authentication.getName();
        SystemUser systemUser = systemUserRepository.findByUsername(systemUserName);
        Company company = machine.getCompany();
        if (systemUser.getCompanies().contains(company)) {
            machineRepository.save(machine);
            return "redirect:/machine/list/" + company.getCompanyId();
        }
        return "error";
    }

    @RequestMapping("/machine/edit/{machineId}")
    public String companyEditMachine(Model model,
                                     @PathVariable("machineId") Long machineId,
                                     @ModelAttribute("companies") List<Company> companies,
                                     Authentication authentication) {
        Machine machine = machineService.findMachineById(machineId);
        if (companies.contains(machine.getCompany())) {
            model.addAttribute("machine", machine);
            return "company/machine-edit";
        }
        return "error";
    }

    @RequestMapping("/machine/edit/update")
    public String companyUpdateMachine(Model model,
                                       @ModelAttribute("companies") List<Company> companies,
                                       Authentication authentication,
                                       @Valid Machine machine,
                                       Errors errors) {
        if (errors.hasErrors()) {
            model.addAttribute("machine", machine);
            return "company/machine-edit";
        }
        Company company = machine.getCompany();
        if (companies.contains(company)) {
            machineRepository.save(machine);
            return "redirect:/machine/list/" + company.getCompanyId();
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
        if (companies.contains(company)) {
            machine.getConservationLogs(); //WTF
            machineRepository.delete(machine);
            return "redirect:/machine/list/" + company.getCompanyId();
        }
        return "error";
    }

}